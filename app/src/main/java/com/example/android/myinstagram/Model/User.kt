package com.example.android.myinstagram.Model

class User {
      lateinit var id:String
      lateinit var bio:String
      lateinit var email:String
      lateinit var imageurl:String
      lateinit var name:String
      lateinit var username:String

      fun User(){
      }
      fun User(name:String,email:String,username:String,bio:String,imageurl:String,id:String){
          this.bio=bio
          this.name=name
          this.email=email
          this.id=id
          this.imageurl=imageurl
          this.username=username
      }



}