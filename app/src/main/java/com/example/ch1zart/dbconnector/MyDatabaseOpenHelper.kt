package com.example.ch1zart.dbconnector

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

class AchivTable(){
    val table_name = "achievements"
    val id = "_id"
    val title = "title"
    val status = "status"
    val description = "description"
}

class TimerTable(){
    val table_name = "Timer"
    val id = "_id"
    val time = "time"
    val pressed = "pressed"
}
class QuestTable(){
    val table_name = "Quest"
    val id = "_id"
    val data_start = "d_start"
    val finish = "finish"
    val complexity = "complexity"
    val t_time = "t_time"
    val t_how = "t_how"
}

class StatistickTable(){

    val table_name = "Statistick"
    val id = "_id"
    val date = "date"
    val time = "time"
    val kkal = "kalories"
    val step = "step"
    val km = "distance"
}

class UserInfo(){

    val table_name = "UserInfo"
    val id = "_id"
    val name = "Name"
    val age = "Age"
    val sex = "Sex"
    val height = "Height"
    val weight = "Weight"
    val haveQuest = "Quest"
}

class MyDatabaseOpenHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "MyDatabase", null, 1) {

    override fun getDatabaseName(): String {
        return super.getDatabaseName()
    }

    companion object {
        private var instance: MyDatabaseOpenHelper? = null
        private val at = AchivTable()
        private val tt = TimerTable()
        private val ut = UserInfo()
        private val st = StatistickTable()
        private val qt = QuestTable()

        @Synchronized
        fun getInstance(ctx: Context): MyDatabaseOpenHelper {
            if (instance == null) {
               //  instance = MyDatabaseOpenHelper(ctx.getApplicationContext())
                instance = MyDatabaseOpenHelper(ctx.applicationContext)
            }
            return instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Here you create tables (more info about that is below)
        createAchiveTable(db)
        createTimerTable(db)
        createUserTable(db)
        createQuestTable(db)
        createStatistickTable(db)
}

    fun createQuestTable(db: SQLiteDatabase){

       db.createTable(qt.table_name, true, qt.id to INTEGER + PRIMARY_KEY, qt.data_start to TEXT, qt.finish to TEXT, qt.complexity to TEXT, qt.t_time to TEXT, qt.t_how to TEXT)
        db.insert(qt.table_name, qt.data_start to "", qt.finish to "",qt.complexity to "", qt.t_time to "" , qt.t_how to "")
    }

    fun createAchiveTable(db: SQLiteDatabase)
    {
        db.createTable(at.table_name, true, at.id to INTEGER + PRIMARY_KEY, at.title to TEXT, at.description to TEXT, at.status to TEXT)
        db.insert(at.table_name, at.title to "На игре", at.description to "Первый запуск приложения",at.status to "true")
        db.insert(at.table_name, at.title to "Отличное начало", at.description to "Запуск первой тренировки",at.status to "false")
        db.insert(at.table_name, at.title to "Шаг вперед", at.description to "Пройти один киллометр",at.status to "false")
        db.insert(at.table_name, at.title to "Шаг вперед 2", at.description to "Пройти пять киллометр",at.status to "false")
        db.insert(at.table_name, at.title to "Шаг вперед 3",at.description to "Пройти пятнадцать киллометр",at.status to "false")
        db.insert(at.table_name, at.title to "Сжигатель", at.description to "Сжечь 1000 калорий",at.status to "false")
        db.insert(at.table_name, at.title to "Дальшелучше", at.description to "Сжечь 10000 калорий",at.status to "false")
    }


   fun createStatistickTable(db: SQLiteDatabase){

      db.createTable(st.table_name, true, st.id to INTEGER + PRIMARY_KEY, st.date to TEXT, st.step to INTEGER ,st.kkal to TEXT, st.km to INTEGER, st.time to TEXT)
       db.insert(st.table_name, st.date to "0", st.kkal to "0", st.km to 0, st.time to "0", st.step to 0)
    }

    fun SaveResultStatistick(db:SQLiteDatabase,date: String, kkal: String, km: Int, time:String, step:Int)
    {
        db.insert(st.table_name, st.date to date, st.kkal to kkal, st.km to km, st.time to time, st.step to step)
    }

    fun createUserTable(db: SQLiteDatabase) {
        db.createTable(ut.table_name, true, ut.id to INTEGER + PRIMARY_KEY, ut.name to TEXT, ut.age to INTEGER, ut.sex to TEXT, ut.height to INTEGER, ut.weight to INTEGER, ut.haveQuest to INTEGER )
        db.insert(ut.table_name, ut.name to "Unreg", ut.age to 0, ut.sex to "", ut.height to 0, ut.weight to 0, ut.haveQuest to 0)
    }

    fun createTimerTable(db: SQLiteDatabase) {
        db.createTable(tt.table_name, true, tt.id to INTEGER + PRIMARY_KEY, tt.time to INTEGER, tt.pressed to TEXT)
        db.insert(tt.table_name, tt.time to 0, tt.pressed to "false")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Here you can upgrade tables, as usual
        onCreate(db)
    }

    fun GetNameUserTable(db:SQLiteDatabase):String
    {
      val name =  db.select(ut.table_name, ut.name).where(ut.id + " = {userid}", "userid" to 1).parseSingle(StringParser)
        return name
    }

    fun GetHeight(db:SQLiteDatabase):Int
    {
        val height = db.select(ut.table_name, ut.height).where(ut.id + " = {userid}", "userid" to 1).parseSingle(IntParser)

        return height
    }

    fun GetQUEST(db:SQLiteDatabase):Int
    {
        val have = db.select(ut.table_name, ut.haveQuest).where(ut.id + " = {userid}", "userid" to 1).parseSingle(IntParser)

        return have
    }


    fun GetWeight(db:SQLiteDatabase):Int
    {
        val weight = db.select(ut.table_name, ut.weight).where(ut.id + " = {userid}", "userid" to 1).parseSingle(IntParser)

        return weight
    }
 }

// Access property for Context
val Context.database: MyDatabaseOpenHelper
    get() = MyDatabaseOpenHelper.getInstance(getApplicationContext())