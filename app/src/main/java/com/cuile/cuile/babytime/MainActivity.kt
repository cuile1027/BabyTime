package com.cuile.cuile.babytime

import android.support.transition.Fade
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import com.cuile.cuile.babytime.addfragment.*
import com.cuile.cuile.babytime.showfragment.ShowMainFragment
import com.cuile.cuile.babytime.utils.DetailsTransition
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.find
import org.jetbrains.anko.uiThread


class MainActivity : BaseActivity() {
    private val showMainFragment by lazy {
        ShowMainFragment.getInstance(
                fabMenuItemClickListener = object : ShowMainFragment.FabMenuItemClickListener{
                    override fun fabMenuItemClicked(clickedId: Int) { changeFragmentById(clickedId) }
                }
        )
    }
    private val bodydataAddFragment by lazy { BodydataAddFragment.getInstance() }
    private val eatAddFragment by lazy { EatAddFragment.getInstance() }
    private val excretionAddFragment by lazy { ExcretionAddFragment.getInstance() }
    private val sleepAddFragment by lazy { SleepAddFragment.getInstance() }

    private lateinit var currentFragment: Fragment

    override fun getContentViewLayout(): Int = R.layout.activity_main

    override fun initViews() {
        setSupportActionBar(mainToolbar)
        currentFragment = showMainFragment
        supportFragmentManager.beginTransaction().add(R.id.mainFragmentContainer, showMainFragment, showMainFragment.javaClass.name)
                .commit()

        mainToolbar.title = getString(R.string.show_main)
    }

    override fun onBackPressed() {
        if (currentFragment != showMainFragment) {
            changeToFragment(showMainFragment)
            mainToolbar.title = getString(R.string.show_main)
        }
        else super.onBackPressed()
    }

    private fun changeFragmentById(id: Int) {
        doAsync {
            Thread.sleep(200)
            uiThread {
                when(id) {
                    R.id.fabMenuToBodyData -> {
                        changeToFragment(bodydataAddFragment)
                        mainToolbar.title = getString(R.string.bodydata_add)
                    }
                    R.id.fabMenuToEatData -> {
                        changeToFragment(eatAddFragment)
                        mainToolbar.title = getString(R.string.eat_add)
                    }
                    R.id.fabMenuToExcretionData -> {
                        changeToFragment(excretionAddFragment)
                        mainToolbar.title = getString(R.string.excretion_add)
                    }
                    R.id.fabMenuToSleepData -> {
                        changeToFragment(sleepAddFragment)
                        mainToolbar.title = getString(R.string.sleep_add)
                    }
                }
            }
        }
    }

    private fun changeToFragment(toFragment: Fragment) {

        if (currentFragment == toFragment) return

        addTransitions(toFragment)

        val transaction = supportFragmentManager.beginTransaction()

        addSharedElement(transaction)

        transaction.replace(R.id.mainFragmentContainer, toFragment).commit()

        currentFragment = toFragment
    }

    private fun addSharedElement(transaction: FragmentTransaction) {
        when(currentFragment) {
            is ShowMainFragment -> {
                transaction.addSharedElement(currentFragment.find(R.id.mainshowFab), getString(R.string.fab_shared_name))
            }
            is BodydataAddFragment -> {
                transaction.addSharedElement(currentFragment.find(R.id.bodydataFab), getString(R.string.fab_shared_name))
            }
            is EatAddFragment -> {
                transaction.addSharedElement(currentFragment.find(R.id.eatFab_stop_or_submit), getString(R.string.fab_shared_name))
            }
            is ExcretionAddFragment -> {
                transaction.addSharedElement(currentFragment.find(R.id.excretionFab), getString(R.string.fab_shared_name))
            }
            is SleepAddFragment -> {
                transaction.addSharedElement(currentFragment.find(R.id.sleepFab), getString(R.string.fab_shared_name))
            }
        }
    }

    private fun addTransitions(fragment: Fragment) {
        fragment.sharedElementEnterTransition = DetailsTransition()
        fragment.sharedElementReturnTransition = DetailsTransition()
        fragment.enterTransition = Fade()
        fragment.exitTransition = Fade()
    }
}
