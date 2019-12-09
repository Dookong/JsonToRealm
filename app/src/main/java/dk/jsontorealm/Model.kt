package dk.jsontorealm

import io.realm.RealmObject

open class Model : RealmObject(){
    var addr: String = ""
    var clCdNm: String = ""
    var distance: String = ""
    var sdrCnt: Int = 0
    var xPos: Double = 0.0
    var yPos: String = ""
    var yadmNm: String = ""
}