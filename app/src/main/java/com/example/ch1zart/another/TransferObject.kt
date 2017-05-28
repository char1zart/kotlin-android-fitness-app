package com.example.ch1zart.another

import com.example.ch1zart.container.ContainerClass
import com.example.ch1zart.dbconnector.ClassExample
import com.example.ch1zart.newsfeed.NewsClass
import com.example.ch1zart.notes.NotesClass
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by Ch1zART on 16.05.2017.
 */
object TransferObject {


    //название фрагмента переключает на нужный фрагмент
    var fragment_state:Int = 0

   //текущий класс для подгрузки
    var current_class: ClassExample? = null

   // текущий фрагмент
   var current_fragment: MainFragment? = null

   //выбор анимации фрагмента
   var set_Animation: String? = null

   //для хранения состояния текущего фрагмента
   var container_state: MutableList<ContainerClass> = ArrayList()

   //для хранения состояния поиска текущего фрагмента
   var container_search: MutableList<ContainerClass> = ArrayList()

   //начальное состояние теории
   var theory_initialization: MutableList<ContainerClass> = ArrayList()
   //начальное состояние правил
   var rules_initialization: MutableList<ContainerClass> = ArrayList()
   //начальное состояние аварийные
   var emergen_initialization: MutableList<ContainerClass> = ArrayList()
   //начальное состояние должности
   var position_initialization: MutableList<ContainerClass> = ArrayList()

  //для избранного
   var favorite_initialization: MutableList<ContainerClass> = ArrayList()

   //текущее состояние нажатого поля
   var current_state: ContainerClass? = null

   var idUser = 1
   var link_i: Int = 0
   var inMenu: AtomicInteger = AtomicInteger(0)
   var for_news_state: MutableList<NewsClass> = ArrayList()
   var haveUser = false
   var status: Int = 1
   var note_status: Int = 0
   var count: Int = 5
   var maxsizeTheory: AtomicInteger = AtomicInteger(0)
   var maxsizePosition: AtomicInteger = AtomicInteger(0)
   var for_note_state: MutableList<NotesClass> = ArrayList()
   var for_init_ns: MutableList<NotesClass> = ArrayList()
   var for_search: MutableList<NotesClass> = ArrayList()

}