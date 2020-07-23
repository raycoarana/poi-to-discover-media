package com.raycoarana.poitodiscover.pipeline.tasks

import com.raycoarana.poitodiscover.core.openSqliteDatabase
import com.raycoarana.poitodiscover.domain.Context
import com.raycoarana.poitodiscover.domain.File
import com.raycoarana.poitodiscover.pipeline.Task
import java.sql.Connection
import javax.inject.Inject


class GenerateMib2HighDatabaseTask @Inject constructor() : Task {
    override fun execute(context: Context) {
        val dbFile = File(context.mib2HighPackageFolder, "poidata.db")
        dbFile.openSqliteDatabase().use { connection ->
            createStructure(connection)

            var rowId = getRowId(connection)
            connection.autoCommit = false

            val poiCoordStatement = connection.prepareStatement("INSERT INTO \"poicoord\" (poiid, latmin, latmax, lonmin, lonmax) VALUES (?, ?, ?, ?, ?)")
            val poiNameStatement = connection.prepareStatement("INSERT INTO \"poiname\" (rowid, name) VALUES (?, ?)")
            val poiDataStatement = connection.prepareStatement("INSERT INTO \"poidata\" (poiid, type, ccode) VALUES (?, ?, ?)")

            context.poiByType.entries.forEach {
                it.value.forEach { poi ->
                    //Coordinate
                    poiCoordStatement.setInt(1, rowId)
                    poiCoordStatement.setString(2, poi.latitude)
                    poiCoordStatement.setString(3, poi.latitude)
                    poiCoordStatement.setString(4, poi.longitude)
                    poiCoordStatement.setString(5, poi.longitude)
                    poiCoordStatement.execute()

                    //Name in FTS
                    poiNameStatement.setInt(1, rowId)
                    poiNameStatement.setString(2, poi.description)
                    poiNameStatement.execute()

                    //Type in data
                    poiDataStatement.setInt(1, rowId)
                    poiDataStatement.setInt(2, poi.type.highId)
                    poiDataStatement.setInt(3, 0)
                    poiDataStatement.execute()

                    rowId++
                }
            }

            connection.commit()
        }
    }

    private fun createStructure(connection: Connection) {
        val statement = connection.createStatement()
        statement.use {
            statement.executeUpdate("create table if not exists \"poidata\" (poiid INTEGER, type INTEGER, namephon TEXT, ccode INTEGER, zipcode TEXT, city TEXT, street TEXT, housenr TEXT, phone TEXT, ntlimportance INTEGER, exttype TEXT, extcont TEXT, warning TEXT, warnphon TEXT, CONSTRAINT PK_poidata PRIMARY KEY (poiid))")
            statement.executeUpdate("create virtual table if not exists \"poiname\" using fts3 (name TEXT)")
            statement.executeUpdate("create virtual table if not exists \"poicoord\" using rtree (poiid INTEGER, latmin REAL, latmax REAL, lonmin REAL, lonmax REAL)")
        }
    }

    private fun getRowId(connection: Connection): Int {
        val statement = connection.createStatement()
        val resultSet = statement.executeQuery("select max(rowid) from \"poicoord\"")
        if (!resultSet.next()) {
            throw Exception("Can not get rowid")
        }
        return resultSet.getInt(1)
    }
}

