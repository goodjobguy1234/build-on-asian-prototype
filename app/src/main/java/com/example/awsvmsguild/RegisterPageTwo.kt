package com.example.awsvmsguild

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.awsvmsguild.data.SignUPData
import com.example.awsvmsguild.data.UserInfo
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import kotlinx.android.synthetic.main.fragment_register_page_two.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterPageTwo.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterPageTwo : Fragment(), DatePickerDialog.OnDateSetListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var signUpInfo: SignUPData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            signUpInfo = it.getParcelable<SignUPData>("signUp")!!
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val items = listOf("Female", "Male")
        val adapter = ArrayAdapter(requireContext(), R.layout.view_list_item, items)
        (menu.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        outlinedTextField3.setStartIconOnClickListener {
            val now = Calendar.getInstance()
            val datePicker = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH), // Initial month selection
                now.get(Calendar.DAY_OF_MONTH)
            )

            datePicker.apply {
                version = DatePickerDialog.Version.VERSION_2
                setAccentColor("#FF6200EE")
                show(this@RegisterPageTwo.parentFragmentManager, "DatePicker")
            }
        }

        prev_btn.setOnClickListener {
            requireActivity().onBackPressed()
        }

        confirm_btn.setOnClickListener {
            val viewList = listOf<EditText>(
                firstName_input, lastName_input, date_input, gender_input
            ).filter {
                it.text.toString().trim() == ""
            }

            viewList.forEach {
                it.error = "Require Attribute"
            }

            if (viewList.isEmpty()) {
                val date = SimpleDateFormat("yyyy/MM/dd").parse(date_input.text.toString())
                val newFormat = SimpleDateFormat("yyyy-MM-dd").format(date)
                (requireActivity() as RegisterActivity).apply {
                    loadingDialog.show()

                    GlobalScope.launch(Dispatchers.IO) {
                        signingUp(
                            signUpInfo.apply {
                                userInfo = UserInfo(
                                    firstname = firstName_input.text.toString(),
                                    lastname = lastName_input.text.toString(),
                                    birthdate = newFormat,
                                    gender = gender_input.text.toString()
                                )
                            }
                        )
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_page_two, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RegisterPageTwo.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegisterPageTwo().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        date_input.setText("${year}/${monthOfYear}/${dayOfMonth}")
    }
}