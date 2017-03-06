package controllers

import javax.inject._

import models.{Accounts, LoginData, Operations}
import play.api.Logger
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._

@Singleton
class LoginController @Inject() extends Controller {

  val loginForm:Form[LoginData] = Form{
   mapping(
     "uname" -> nonEmptyText,
     "psw" -> nonEmptyText
   )(LoginData.apply)(LoginData.unapply)

  }

def showProfile(username:String)= Action { implicit  request =>

  val users = Operations.getAllUsers
  val result = users.map(user=>if(user.uname == username) Some(user) else None)
  Ok(views.html.profile(result.flatten.toList, request))
}

  def processForm = Action{ implicit request =>
    loginForm.bindFromRequest.fold(
      formErrors => {
        Redirect(routes.HomeController.index()).flashing("msg"->"Incorrect username or password")
      },
      LoginData => {
         val user = Operations.getAllUsers
         val result =  user.map( x=>

              if(x.uname == LoginData.username && x.pswd == LoginData.password) true
              else false
         )

         if(result.contains(true)){

           Redirect(routes.LoginController.showProfile(LoginData.username)).withSession("currentUser"->LoginData.username).flashing("msg"->"Login Successful")
         }
         else
           Redirect(routes.HomeController.index()).flashing("msg"->"Incorrect username or password")

      }
    )


  }

  def logout = Action{ implicit request=>

    Redirect(routes.HomeController.index()).withNewSession
  }



}
