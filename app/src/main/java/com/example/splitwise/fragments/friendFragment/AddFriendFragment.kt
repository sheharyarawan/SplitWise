package com.example.splitwise.fragments.friendFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.splitwise.R
import com.example.splitwise.databinding.FragmentAddFriendBinding
import com.example.splitwise.viewModels.FriendViewModel


class AddFriendFragment : Fragment(R.layout.fragment_add_friend) {

    lateinit var binding: FragmentAddFriendBinding
    val args: AddFriendFragmentArgs by navArgs()
    val friendViewModel: FriendViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding= FragmentAddFriendBinding.bind(view)
        setUpToolbarClicks()

    }

    fun setUpToolbarClicks(){
        binding.materialToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding.addFriendNextButton.setOnClickListener {
            addUserToGroup()
        }
    }

    fun addUserToGroup(){
        val groupId= args.groupId
        val name= binding.nameEditText.text.toString().trim()
        val email= binding.phoneOrEmailEditText.text.toString().trim()

        friendViewModel.addUserToGroup(groupId,email,name,
            onSuccess = {
                findNavController().navigateUp()
            },
            onFailure = {
                Toast.makeText(requireContext(),"User already exists",
                    Toast.LENGTH_SHORT).show()
            })
    }
}