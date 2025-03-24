package es.architectcoders.spaceexplorer.ui.main

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

fun waitFor(millis: Long): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return ViewMatchers.isRoot()
        }
        override fun getDescription(): String {
            return "esperar $millis milisegundos"
        }
        override fun perform(uiController: UiController, view: View) {
            uiController.loopMainThreadForAtLeast(millis)
        }
    }
}

fun clickChildViewWithId(id: Int): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View>? {
            return null
        }

        override fun getDescription(): String {
            return "Click en un hijo específico del ViewHolder"
        }

        override fun perform(uiController: UiController, view: View) {
            val v = view.findViewById<View>(id)
            v.performClick()
        }
    }
}

object RecyclerViewMatchers {
    fun atPosition(position: Int, recyclerViewId: Int): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("View at position $position in recyclerView with id $recyclerViewId")
            }

            override fun matchesSafely(view: View): Boolean {
                val recyclerView = view.rootView.findViewById<RecyclerView>(recyclerViewId)
                val viewHolder = recyclerView.findViewHolderForAdapterPosition(position)
                return viewHolder?.itemView == view
            }
        }
    }
}