package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val application = requireNotNull(this.activity).application
        val adapter = AsteroidsAdapter(AsteroidsAdapter.AsteroidListener { asteroidID ->
            viewModel.onAsteroidClicked(asteroidID)
        })

        //show detail fragment onclick on an asteroid
        viewModel.navigateToAsteroidDetail.observe(viewLifecycleOwner) { asteroid ->
            asteroid?.let {

                findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))

                //tell the fragment that navigation was done
                this.viewModel.onAsteroidDetailNavigated()
            }
        }

        viewModel.asteroids.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        binding.asteroidRecycler.adapter = adapter
        setHasOptionsMenu(true)
        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.optionMenu.value =
            when(item.itemId){
                //shows all saved asteroids
                R.id.show_all_menu-> {
                    MainViewModel.OptionMenu.SHOW_ALL
                }
                //showed asteroids of today
                R.id.show_today_menu -> MainViewModel.OptionMenu.SHOW_TODAY
                //shows asteroid for the week
                else->MainViewModel.OptionMenu.SHOW_WEEK
            }

        Snackbar.make(this.requireView(),
            when(item.itemId){
                R.id.show_saved_menu->getString(R.string.saved_asteroids)
                R.id.show_today_menu -> getString(R.string.today_asteroids)
                else-> getString(R.string.next_week_asteroids) } ,
            Snackbar.LENGTH_LONG)
            .show()
        return true
    }
}
