package es.architectcoders.spaceexplorer

import androidx.test.rule.GrantPermissionRule
import org.junit.Rule

class RoversInstrumentationTest {

    @get:Rule
    val storagePermissionRule: GrantPermissionRule = GrantPermissionRule.grant(

    )
}