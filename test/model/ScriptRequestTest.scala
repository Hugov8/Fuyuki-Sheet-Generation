package model

import org.scalatest.flatspec.AnyFlatSpec
import model.connecteur.ScriptRequestRayshift
import model.connecteur.ScriptRequestAtlas

import model.data._
import model.parser.RayshiftParser
import model.connecteur.WarRequestRayshift

class ScriptRequestTest extends AnyFlatSpec {
    "List of id row" should "be" in {
        val res = List("9400910110", "9400910111", "9400910210", "9400910211", "9400910310", "9400910311", "9400910410", "9400910420", "9400910421", "9400910510", "9400910511",
        "9400910610", "9400910611", "9400910710", "9400910720", "9400910721")
        assert(res==WarRequestRayshift.getListIdRow("8091"))
    }

    "Url's result from build main" should "match" in {
        assert(ScriptRequestRayshift.buildURL("100072051")=="https://rayshift.io/api/v1/translate/script/forward/100072051?apiKey="+ScriptRequestRayshift.key)
        assert(ScriptRequestAtlas.buildURL("100072051")=="https://static.atlasacademy.io/NA/Script/01/0100072051.txt")
    }

    "Url's result from build event" should "match" in {
        assert(ScriptRequestRayshift.buildURL("9400910111")=="https://rayshift.io/api/v1/translate/script/forward/9400910111?apiKey="+ScriptRequestRayshift.key)
        assert(ScriptRequestAtlas.buildURL("9400910111")=="https://static.atlasacademy.io/NA/Script/94/9400/9400910111.txt")
    }

    "Parsing the row 2150" should "match" in {
        val lines: List[Line] = List(Line("550318","1","0", NPC("Dr. Roman","Dr.ロマン"),"You've finally caught up to the head, but Beast II is only three hundred meters from the surface!","ついに頭部に追いついた！ ビーストⅡ、地上まであと三百メートル！"),
        Line("550319","1","1",NPC("Dr. Roman","Dr.ロマン"), "Hurry! At the rate she's going, she'll only need a few more minutes to climb out! Destroy her core, no matter what!","あの巨体なら数分もかからない！ なんとしても本体を叩くんだ！"),
        Line("550320","1","2",NPC("Mash","マシュ"),"Understood...! Master, your orders, please! Beast II cleanup operation begins now!","はい……！　マスター、指示を！ これよりビーストⅡ掃討戦を開始します！")
        )
        val res: Row = Row("100072150", lines)
        assert(ScriptRequestRayshift.getRowScript("100072150") == res)
    }

    "Parsing the row 2161" should "match" in {
        val lines: List[Line] = List(Line("451758","1","0",NPC("Mash","マシュ"),"We've detached Beast II from the wall! She's falling to the bottom of the underworld!","ビーストⅡ、壁から[#剥離:はくり]！ 冥界の底に落ちていきます！"),
        Line("451759","1","1",NPC("Dr. Roman","Dr.ロマン"),"Spirit Origin collapse confirmed. Eleven Magical Reactor Cores have gone critical!","霊基崩壊を確認、魔力炉心11基、すべて臨界を確認！"),
        Line("451760","1","2",NPC("Dr. Roman","Dr.ロマン"),"Everyone hurry and take refuge above in Uruk! Beast II is going to explode!","みんな、急いで上空、ウルクに退避を！ ビーストⅡ、爆散するぞ[line 5]！"),
        Line("451761","1","3",NPC("Fou","フォウ"),"Fou, fooooou!","フォウ、フォ[line 3]ウ！"),
        Line("451762","1","4",NPC("Mash","マシュ"),"Master, give me your hand...!","マスター、手を……！"),
        Line("451763","2","0",Master("1"),"Mash...!","マシュ……！"))
        val res: Row = Row("100072161", lines)
        assert(ScriptRequestRayshift.getRowScript("100072161")==res)
    }

    "Parsing the row 2161 with Atals" should "match" in {
        val lines: List[Line] = List(Line(NA(), NA(), NA(), NPC("Mash",""),"We've detached Beast II from the wall! She's falling to the bottom of the underworld!",""),
        Line(NA(), NA(), NA(),NPC("Dr. Roman",""),"Spirit Origin collapse confirmed. Eleven Magical Reactor Cores have gone critical!",""),
        Line(NA(), NA(), NA(),NPC("Dr. Roman",""),"Everyone hurry and take refuge above in Uruk! Beast II is going to explode!",""),
        Line(NA(), NA(), NA(),NPC("Fou",""),"Fou, fooooou!",""),
        Line(NA(), NA(), NA(),NPC("Mash",""),"Master, give me your hand...!",""),
        Line(NA(), NA(), NA(),Master("1"),"Mash...!",""))
        val res: Row = Row("100072161", lines)
        assert(ScriptRequestAtlas.getRowScript("100072161")==res)
    }

    "Parse and unparse" should "match" in {
        val script: String = ScriptRequestRayshift.getRowScriptText("100072161")
        assert(RayshiftParser.unparse(RayshiftParser.parse(script))==script)
    }

    "List of script" should "match" in {
        val res = List("9400090110","9400090111","9400090210","9400090211","9400090310","9400090320","9400090321","9400090410","9400090411","9400090510","9400090511","9400090610","9400090611")
        assert(WarRequestRayshift.getListIdRow("8090")==res)
    }

    "Name of war" should "match" in {
        val res = "Lostbelt No.5\nAncient Ocean of the Dreadnought Gods, Atlantis"
        assert(res==WarRequestRayshift.getNameRow("305"))
    }

}