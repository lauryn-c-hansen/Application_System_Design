package edu.msd

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table

object ManyPosts: IntIdTable(){
    val PostText = varchar("text", 100)
    val timeStamp = long("timeStamp")
}
