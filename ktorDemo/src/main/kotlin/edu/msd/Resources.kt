package edu.msd
import edu.msd.ManyPosts.PostText
import edu.msd.ManyPosts.timeStamp
import io.ktor.http.HttpMethod.Companion.Post
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.Resources
import io.ktor.server.response.*
import io.ktor.server.routing.routing

import io.ktor.server.sessions.*
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.greaterEq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction


fun Application.configureResources() {
    install(Resources)
    routing{
        get<Posts> {
            //handler for /books
            call.respond(
                newSuspendedTransaction(Dispatchers.IO) {
                    //Query table directly
                    ManyPosts
                        .selectAll()
                        .map{PostData(it[PostText], it[ManyPosts.timeStamp], it[ManyPosts.id].value)}
                }
            )
        }

        get<Posts.getID> {
            //handler for /books
            call.respond(
                newSuspendedTransaction(Dispatchers.IO) {
                    //Query table directly
                    ManyPosts
                        .select(ManyPosts.id eq it.id)
                        .map{PostData(it[PostText], it[ManyPosts.timeStamp], it[ManyPosts.id].value)}
                }
            )
        }

        //todo: update name to be something else like get from last jfshdfjlsdfhjsdfhsdj
        get<Posts.getTimeStamp> {
            //handler for /books
            call.respond(
                newSuspendedTransaction(Dispatchers.IO) {
                    //Query table directly
                    ManyPosts
                        .select(ManyPosts.timeStamp greaterEq it.timestamp)
                        .map{PostData(it[PostText], it[ManyPosts.timeStamp], it[ManyPosts.id].value)}
                }
            )
        }


        post<Posts> {
            val postInput = call.receive<getPostData>()
            newSuspendedTransaction(Dispatchers.IO, DBSettings.db) {
                ManyPosts.insert{
                       //it is the current Post object
                        it[PostText] = postInput.text
                        it[timeStamp] = System.currentTimeMillis()
                } get ManyPosts.id
            }
            call.respondText("Posted...$postInput")
        }

        delete<Posts.Delete> {
            val id = it.id
            val deletedRows = newSuspendedTransaction(Dispatchers.IO) {
                ManyPosts.deleteWhere { ManyPosts.id eq id }
            }

            if(deletedRows > 0){
                call.respondText { "$id row(s) of interest deleted" }
            }
            else{
                call.respondText { "$id row(s) of interest not found....sad" }
            }
        }

    }
}


@Serializable data class PostData(val text: String, val time:Long, val ID: Int)
@Serializable data class getPostData(val text: String)


@Resource("/posts") //corresponds to /books
class Posts {
    @Resource("{timestamp}/timestamp") //corresponds to /books/liked
    class getTimeStamp(val parent: Posts = Posts(), val timestamp: Long)


    @Resource("{id}/id") //corresponds to /books/like?title=some%20book
    class getID(val parent: Posts = Posts(), val id: Int)

    @Resource("{id}/delete") //corresponds to /books/{id}/hate
    class Delete(val parent: Posts = Posts(), val id: Int)
}
