package models
import services.BackendService

import scala.collection.mutable.ListBuffer

object Operations {

   def addUser(user:Accounts) = BackendService.listOfAccounts+=user

   def getAllUsers:ListBuffer[Accounts]= BackendService.listOfAccounts

}
