package controllers

import javax.inject._

import models.{Accounts, LoginData, Operations}
import play.api.Logger
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._

@Singleton
class SignupController @Inject() extends Controller {

 val RegForm:Form[Accounts] = Form{

   mapping(
      "fname" -> nonEmptyText,
      "mname" -> text,
      "lname" -> nonEmptyText,
      "uname" -> nonEmptyText,
      "psw" -> nonEmptyText,
      "repsw" -> nonEmptyText,
      "mobile" -> nonEmptyText,
      "gender" ->nonEmptyText,
      "age" -> number(min = 18, max = 75),
      "hobby" -> nonEmptyText
   )(Accounts.apply)(Accounts.unapply)

 }

  def processForm = Action{ implicit request =>
    RegForm.bindFromRequest.fold(
      formErrors => {
        Redirect(routes.HomeController.index()).flashing("masterMsg"->"something Went Wrong,Try Later")
      },
      LoginData => {
        val users = Operations.getAllUsers
        val account = RegForm.bindFromRequest.get
        val result =  users.map( x=>

          if(x.uname != account.uname) true
          else false
        )
        if(!result.contains(false)){

            if(account.pswd==account.repswd){

                if(account.mobile.length==10){
                  Operations.addUser(account)
                  Redirect(routes.LoginController.showProfile(LoginData.uname)).withSession("currentUser"->LoginData.uname).flashing("msg"->"Registration Successful")
                }
                else{
                  Redirect(routes.HomeController.index()).flashing("mobile"->"Invalid Mobile Number")
                }

            }
            else{

              Redirect(routes.HomeController.index()).flashing("passMismatch"->"Pasword dosent Match")
            }

        }
        else
          Redirect(routes.HomeController.index()).flashing("alreadyExist"->"Username Already Taken")

      }
    )

  }


}
