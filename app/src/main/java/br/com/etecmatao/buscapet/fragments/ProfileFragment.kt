package br.com.etecmatao.buscapet.fragments

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.com.etecmatao.buscapet.R
import br.com.etecmatao.buscapet.viewModel.ProfileViewModel
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    private lateinit var vm: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm = ViewModelProvider(requireActivity()).get(ProfileViewModel::class.java)
        vm.loadCurrentUser()

        registerObservables()
        registerButtonListeners()
    }

    private fun registerObservables() {
        vm.mayChangeProfile.observe(viewLifecycleOwner, Observer {
            it?.let { editing ->
                txtFirstName.isEnabled = editing
                txtLastName.isEnabled = editing
                txtEmail.isEnabled = editing
            }
        })

        vm.user.observe(viewLifecycleOwner, Observer {
            it?.let { usr ->
                txtFirstName.setText(usr.firstName)
                txtLastName.setText(usr.lastName)
                txtEmail.setText(usr.email)
            }
        })
    }

    private fun registerButtonListeners() {
        btnEdit.setOnClickListener { vm.startUpdatingProfile() }
        btnSave.setOnClickListener {
            val user = vm.user.value

            user?.let { usr ->
                usr.email = txtEmail.text.toString()
                usr.lastName = txtLastName.text.toString()
                usr.firstName = txtFirstName.text.toString()

                vm.saveProfile(usr) {
                    vm.endUpdatingProfile()
                }
            }
        }

        btnChangePassword.setOnClickListener {
            val oldPassword = txtChangeOldPassword.text.toString()
            val newPassword = txtChangeNewPassword.text.toString()
            val confirmNewPassword = txtChangeConfirmNewPassword.text.toString()

            if (TextUtils.isEmpty(newPassword) ||
                TextUtils.isEmpty(confirmNewPassword) ||
                newPassword != confirmNewPassword
            ) {

                Toast.makeText(
                    requireActivity(),
                    resources.getString(R.string.msg_password_does_not_match),
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            vm.updatePassword(
                oldPassword,
                newPassword,
                onSuccess = {
                    Toast.makeText(
                        requireActivity(),
                        resources.getString(R.string.msg_password_changed),
                        Toast.LENGTH_SHORT
                    ).show()

                    txtChangeOldPassword.setText("")
                    txtChangeNewPassword.setText("")
                    txtChangeConfirmNewPassword.setText("")
                },
                onInvalidPassword = {
                    Toast.makeText(
                        requireActivity(),
                        resources.getString(R.string.msg_old_password_invalid),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }
    }
}