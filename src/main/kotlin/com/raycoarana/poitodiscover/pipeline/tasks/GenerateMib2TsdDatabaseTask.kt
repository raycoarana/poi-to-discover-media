package com.raycoarana.poitodiscover.pipeline.tasks

import com.raycoarana.poitodiscover.core.MortonEncoder
import com.raycoarana.poitodiscover.core.Translations
import com.raycoarana.poitodiscover.core.openSqliteDatabase
import com.raycoarana.poitodiscover.domain.Context
import com.raycoarana.poitodiscover.domain.File
import com.raycoarana.poitodiscover.pipeline.Task
import java.sql.Connection
import javax.inject.Inject

class GenerateMib2TsdDatabaseTask @Inject constructor(
        private val translations: Translations,
        private val mortonEncoder: MortonEncoder
) : Task {
    override fun execute(context: Context) {
        val dbFile = File(context.mib2TsdPackageFolder, "poidata.db3")
        dbFile.openSqliteDatabase().use { connection ->
            createStructure(connection, context)

            var rowId = getRowId(connection)

            val poiCoordStatement = connection.prepareStatement("INSERT INTO pPoiAddressTable (pPoiId, catId, mortonCode, name) VALUES (?, ?, ?, ?)")
            val poiNameStatement = connection.prepareStatement("INSERT INTO pPoiFtsTable (pPoiId, name) VALUES (?, ?)")
            val poiSystemStatement = connection.prepareStatement("INSERT INTO pPoiSystemTable (pPoiId, catId) VALUES (?, ?)")

            insertCategories(connection, context)
            insertIcons(connection, context)

            context.poiByType.entries.forEach {
                it.value.forEach { poi ->
                    //pPoiAddressTable
                    poiCoordStatement.setInt(1, rowId)
                    poiCoordStatement.setInt(2, poi.type.tsdId)
                    poiCoordStatement.setLong(3, mortonEncoder.encode(poi.latitude.toDouble(), poi.longitude.toDouble()))
                    poiCoordStatement.setString(4, poi.description)
                    poiCoordStatement.execute()

                    //Name in FTS
                    poiNameStatement.setInt(1, rowId)
                    poiNameStatement.setString(2, poi.description)
                    poiNameStatement.execute()

                    //pPoiSystemTable
                    poiSystemStatement.setInt(1, rowId)
                    poiSystemStatement.setInt(2, poi.type.tsdId)
                    poiSystemStatement.execute()

                    rowId++
                }
            }
        }
    }

    private fun createStructure(connection: Connection, context: Context) {
        val statement = connection.createStatement()
        statement.use {
            statement.executeUpdate("CREATE TABLE infoDB( nameDB TEXT NOT NULL,dataModel TEXT,version TEXT,comment TEXT,DBTSConform INTEGER NOT NULL,isDirty INTEGER )")
            statement.executeUpdate("INSERT INTO infoDB(nameDB,dataModel,version,comment,DBTSConform,isDirty) VALUES (\"${context.name}\",\"1.1.1\",\"1.0\",\"${context.name}\",0,0)")

            statement.executeUpdate("CREATE TABLE pPoiAddressTable( pPoiId INTEGER unique primary key,catId INTEGER NOT NULL,mortonCode INTEGER NOT NULL,name TEXT NOT NULL,stateAbbreviation TEXT ,country TEXT ,province TEXT ,city TEXT ,street TEXT,streetnumber TEXT ,junction TEXT ,zipCode TEXT ,telephone TEXT ,contacts BLOB ,extContent BLOB ,version TEXT,isDirty INTEGER , foreign key(catId) REFERENCES pPoiCategoryTable(catId) ON UPDATE CASCADE)")
            statement.executeUpdate("CREATE TABLE pPoiCategoryTable( catId INTEGER UNIQUE PRIMARY KEY,categoryDefaultName TEXT NOT NULL UNIQUE,warning INTEGER NOT NULL,warnMessage TEXT ,catScaleLevel INTEGER ,activationRadius INTEGER,phoneticString TEXT ,isLocalCategory INTEGER,version TEXT ,isDirty INTEGER )")
            statement.executeUpdate("CREATE VIRTUAL TABLE pPoiFtsTable USING fts4 ( pPoiId INTEGER NOT NULL,name TEXT NOT NULL)")
            statement.executeUpdate("CREATE TABLE pPoiIconTable( iconId INTEGER primary key,catId INTEGER NOT NULL,iconSet INTEGER NOT NULL,scaleLevel INTEGER ,collectionId INTEGER ,usageTypeMask INTEGER ,iconDisplayArrangement INTEGER,iconDrawingPriority INTEGER ,version TEXT ,iconName TEXT NOT NULL,iconImage BLOB , foreign key(catId) REFERENCES pPoiCategoryTable(catId))")
            statement.executeUpdate("CREATE TABLE pPoiSystemTable( pPoiId INTEGER UNIQUE PRIMARY KEY,catId INTEGER NOT NULL,priority INTEGER,sortIndex INTEGER ,personalComment TEXT ,str1 TEXT ,str2 TEXT ,int1 INTEGER ,int2 INTEGER ,version TEXT,isDirty INTEGER , FOREIGN KEY(catId) REFERENCES pPoiCategoryTable(catId) ON UPDATE CASCADE)")
        }
    }

    private fun getRowId(connection: Connection): Int {
        val statement = connection.createStatement()
        val resultSet = statement.executeQuery("select max(rowid) from pPoiAddressTable")
        if (!resultSet.next()) {
            throw Exception("Can not get rowid")
        }
        return resultSet.getInt(1)
    }

    private fun insertCategories(connection: Connection, context: Context) {
        val statement = connection.createStatement()
        statement.use {
            context.poiByType.keys.forEach { poiType ->
                val name = translations.get(poiType.toString())
                statement.executeUpdate("INSERT INTO pPoiCategoryTable (catId, categoryDefaultName, warning) VALUES (${poiType.tsdId},\"$name\",1)")
            }
        }
    }

    private fun insertIcons(connection: Connection, context: Context) {
        val statement = connection.createStatement()
        statement.use {
            var iconId = 1
            context.poiByType.keys.forEach { poiType ->
                statement.executeUpdate("INSERT INTO pPoiIconTable (iconId, catId, iconSet, iconName) VALUES ($iconId,\"${poiType.tsdId}\",1, \"${poiType.image}\")")
                iconId ++
                statement.executeUpdate("INSERT INTO pPoiIconTable (iconId, catId, iconSet, iconName) VALUES ($iconId,\"${poiType.tsdId}\",2, \"${poiType.image}\")")
                iconId ++
            }
        }
    }
}
