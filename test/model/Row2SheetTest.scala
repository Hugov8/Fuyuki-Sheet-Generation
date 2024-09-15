package model

import org.scalatest.flatspec.AnyFlatSpec
import model.connecteur.ScriptRequestRayshift
import model.connecteur.ScriptRequestAtlas
import model.connecteur.sheets.SheetsServiceUtil

import model.data._
import model.parser.RayshiftParser
import model.connecteur.WarRequestRayshift
import com.google.api.services.sheets.v4.model.ValueRange

import java.util.Arrays
import model.connecteur.sheets.UpdateSheetUtil

class Row2SheetTest extends AnyFlatSpec {
    "Row conversion" should "match" in {
        val lines: List[Line] = List(Line("451758","1","0",NPC("Mash","マシュ"),"We've detached Beast II from the wall! She's falling to the bottom of the underworld!","ビーストⅡ、壁から[#剥離:はくり]！ 冥界の底に落ちていきます！"),
        Line("451759","1","1",NPC("Dr. Roman","Dr.ロマン"),"Spirit Origin collapse confirmed. Eleven Magical Reactor Cores have gone critical!","霊基崩壊を確認、魔力炉心11基、すべて臨界を確認！"),
        Line("451760","1","2",NPC("Dr. Roman","Dr.ロマン"),"Everyone hurry and take refuge above in Uruk! Beast II is going to explode!","みんな、急いで上空、ウルクに退避を！ ビーストⅡ、爆散するぞ[line 5]！"),
        Line("451761","1","3",NPC("Fou","フォウ"),"Fou, fooooou!","フォウ、フォ[line 3]ウ！"),
        Line("451762","1","4",NPC("Mash","マシュ"),"Master, give me your hand...!","マスター、手を……！"),
        Line("451763","2","0",Master("1"),"Mash...!","マシュ……！"))
        val res: Row = Row(IdRow("100072161", "100072161"), lines)

        val body = new ValueRange().setValues(Arrays.asList(
            Arrays.asList("451758","1","0","Mash","マシュ","We've detached Beast II from the wall! She's falling to the bottom of the underworld!","ビーストⅡ、壁から[#剥離:はくり]！ 冥界の底に落ちていきます！"),
            Arrays.asList("451759","1","1","Dr. Roman","Dr.ロマン","Spirit Origin collapse confirmed. Eleven Magical Reactor Cores have gone critical!","霊基崩壊を確認、魔力炉心11基、すべて臨界を確認！"),
            Arrays.asList("451760","1","2","Dr. Roman","Dr.ロマン","Everyone hurry and take refuge above in Uruk! Beast II is going to explode!","みんな、急いで上空、ウルクに退避を！ ビーストⅡ、爆散するぞ[line 5]！"),
            Arrays.asList("451761","1","3","Fou","フォウ","Fou, fooooou!","フォウ、フォ[line 3]ウ！"),
            Arrays.asList("451762","1","4","Mash","マシュ","Master, give me your hand...!","マスター、手を……！"),
            Arrays.asList("451763","2","0","1", "1","Mash...!","マシュ……！"),

        )
        )
        assert(body.equals(UpdateSheetUtil.row2ValueRange(res)))

    }
}