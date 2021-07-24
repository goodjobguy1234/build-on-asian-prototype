package com.example.awsvmsguild

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.awsvmsguild.data.SignUPData
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.fragment_register_page_one.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterPageOne.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterPageOne : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        next_btn.setOnClickListener {
            val viewList = listOf<TextInputEditText>(
                password_input, repassword_input, email_input, user_input
            ).filter {
                it.text!!.trim().toString() == ""
            }

            if(viewList.isEmpty()) {
                if (password_input.text.toString() == repassword_input.text.toString()) {
                    findNavController().navigate(R.id.action_registerPageOne_to_registerPageTwo,
                        bundleOf("signUp" to SignUPData(
                            email = email_input.text.toString(),
                            password = password_input.text.toString(),
                            username = user_input.text.toString()
                        ))
                    )
                } else {
                    password_input.error = "differ password"
                    repassword_input.error = "differ password"
                }

            } else {
                viewList.forEach {
                    it.error = "Required Attribute"
                }
            }

        }

        cancel_btn.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_page_one, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RegisterPageOne.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegisterPageOne().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}