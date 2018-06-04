package com.example.jonnyb.smack.Controller

import android.content.Context
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.jonnyb.smack.R
import com.example.jonnyb.smack.Services.AuthService
import android.widget.Toast
import com.example.jonnyb.smack.Services.UserDataService.email
import kotlinx.android.synthetic.main.activity_create_user.*
import java.util.*


class CreateUserActivity : AppCompatActivity() {

    var userAvatar = "profileDefault"
    var avatarColor = "[0.5, 0.5, 0.5, 1]"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)
        createSpinner.visibility = View.INVISIBLE
    }

    fun generateUserAvatar(view: View) {
        val random = Random()
        val color = random.nextInt(2)
        val avatar = random.nextInt(28)

        if (color == 0) {
            userAvatar = "light$avatar"
        } else {
            userAvatar = "dark$avatar"
        }
        val resourceId = resources.getIdentifier(userAvatar, "drawable", packageName)
        createAvatarImageView.setImageResource(resourceId)
    }

    fun generateColorClicked(view: View) {
        val random = Random()
        val r = random.nextInt(255)
        val g = random.nextInt(255)
        val b = random.nextInt(255)

        createAvatarImageView.setBackgroundColor(Color.rgb(r,g,b))

        val savedR = r.toDouble() / 255
        val savedG = g.toDouble() / 255
        val savedB = b.toDouble() / 255

        avatarColor = "[$savedR, $savedG, $savedB, 1]"
    }

    fun createUserClicked(view: View) {
        val userName = createUserNameText.text.toString()
        val emailName = createEmailText.text.toString()
        val password = createPasswordText.text.toString()
        enableSpinner(true)
        if(userName.isNotEmpty() && emailName.isNotEmpty() && password.isNotEmpty()){
            AuthService.registerUser(this, emailName, password) { registerSuccess ->
                if (registerSuccess) {
                    Log.d("1", "User successfully registered");
                    AuthService.loginUser(this, emailName, password) {loginSuccess ->
                        if(loginSuccess){
                            AuthService.createUser(this, userName, emailName, userAvatar, avatarColor){
                                createSuccess ->
                                if(createSuccess){
                                    enableSpinner(false)
                                    finish()
                                } else {
                                    errorToast()
                                }
                            }
                        }else {
                            errorToast()
                        }
                    }
                }else{
                    errorToast()
                }
            }
        }else{
            Toast.makeText(this, "Something went wrong, please try again.", Toast.LENGTH_LONG)
            enableSpinner(false)
        }

    }

    fun errorToast(){
        Toast.makeText(this, "Somthing went wrong, please try again.", Toast.LENGTH_LONG).show()
        enableSpinner(false)
    }

    fun enableSpinner(enable:Boolean) {
        if(enable) {
            createSpinner.visibility = View.VISIBLE
        }else{
            createSpinner.visibility = View.INVISIBLE
        }
        createUserBtn.isEnabled = !enable
        createAvatarImageView.isEnabled = !enable
        backgroundColorBtn.isEnabled = !enable
    }
}
