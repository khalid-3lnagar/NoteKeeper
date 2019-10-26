package khalid.elnagar.notekeeper.presentation.core

import android.app.Activity
import android.os.Handler
import android.os.Looper.getMainLooper
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import khalid.elnagar.notekeeper.R
import kotlinx.android.synthetic.main.activity_note_list.*

fun DrawerLayout.close() = closeDrawer(GravityCompat.START)
fun DrawerLayout.openDelayed(delayMillis: Long) {
    Handler(getMainLooper())
        .postDelayed({ openDrawer(GravityCompat.START) }, delayMillis)
}

fun DrawerLayout.addNavToggle(activity: Activity) {
    ActionBarDrawerToggle(
        activity, this,
        activity.toolbar, R.string.open_nav_drawer, R.string.close_nav_drawer
    ).also(::setDrawerListener)
        .apply { syncState() }

}