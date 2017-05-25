package com.example.ch1zart.another

import com.example.ch1zart.newsfeed.NewsClass
import com.example.ch1zart.notes.NotesClass
import com.example.ch1zart.position.PositionClass
import com.example.ch1zart.theory.TheoryClass
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by Ch1zART on 16.05.2017.
 */
object TransferObject {

   var for_theory_state: MutableList<TheoryClass> = ArrayList()
   var for_theory_search: MutableList<TheoryClass> = ArrayList()
   var for_favorite_state: MutableList<PositionClass> = ArrayList()
   var for_position_state:  MutableList<PositionClass> = ArrayList()
   var idUser = 1
   var link_i: Int = 0
   var inMenu: AtomicInteger = AtomicInteger(0)
   var for_news_state: MutableList<NewsClass> = ArrayList()
   var haveUser = false
   var status: Int = 1
   var note_status: Int = 0
   var count: Int = 1
   var maxsizeTheory: AtomicInteger = AtomicInteger(0)
   var maxsizePosition: AtomicInteger = AtomicInteger(0)
   var for_note_state: MutableList<NotesClass> = ArrayList()
   var for_init_ns: MutableList<NotesClass> = ArrayList()
   var for_search: MutableList<NotesClass> = ArrayList()

}