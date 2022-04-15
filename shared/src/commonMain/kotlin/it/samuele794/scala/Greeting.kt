package it.samuele794.scala

class Greeting {
    fun greeting(): String {
        return "Hello, ${Platform().platform}!"
    }
}