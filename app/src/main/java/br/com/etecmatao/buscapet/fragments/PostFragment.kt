package br.com.etecmatao.buscapet.fragments

import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.etecmatao.buscapet.R
import br.com.etecmatao.buscapet.chat.MessageAdapter
import br.com.etecmatao.buscapet.model.AdType
import br.com.etecmatao.buscapet.model.Message
import br.com.etecmatao.buscapet.viewModel.ChatViewModel
import br.com.etecmatao.buscapet.viewModel.PostViewModel
import kotlinx.android.synthetic.main.fragment_post.*
import kotlinx.android.synthetic.main.pet_lost_confirm_picture_fragment.*
import java.util.*

class PostFragment : Fragment() {
    private lateinit var vm: PostViewModel
    private lateinit var chatViewModel: ChatViewModel
    private val args: PostFragmentArgs by navArgs()

    private lateinit var adapter: MessageAdapter

    private lateinit var chatID: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adapter = MessageAdapter(requireContext())

        vm = ViewModelProvider(this).get(PostViewModel::class.java)
        chatViewModel = ViewModelProvider(this).get(ChatViewModel::class.java)

        chatViewModel.loadCurrentUser()

        return inflater.inflate(R.layout.fragment_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerObservables()

        btnSendMessage.setOnClickListener {
            chatViewModel.user.value?.let { user ->
                val message = Message(user = user, text = txtMessage.text.toString())

                chatViewModel.sendMessage(chatID, message)
                txtMessage.setText("")
            }
        }

        btnSolve.setOnClickListener {
            vm.markAsResolved(vm.post.value!!){
                findNavController().popBackStack()
            }
        }

        btnDelete.setOnClickListener {
            vm.markAsResolved(vm.post.value!!){
                findNavController().popBackStack()
            }
        }

        messagesView.adapter = adapter
        messagesView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onStart() {
        super.onStart()

        vm.loadPost(args.postID)
    }

    private fun registerObservables(){
        chatViewModel.user.observe(viewLifecycleOwner, Observer {
            adapter.setUser(it)
        })

        chatViewModel.messages.observe(viewLifecycleOwner, Observer {
            adapter.setMessages(it)
        })

        vm.mayResolve.observe(viewLifecycleOwner, Observer {
            it?.let { allowed ->
                btnSolve.visibility = if (allowed) VISIBLE else GONE
                btnDelete.visibility = if (allowed) VISIBLE else GONE
            }
        })

        vm.post.observe(viewLifecycleOwner, Observer {
            txtPostTitle.text = it.title
            txtPostAuthor.text = it.user?.firstName ?: "Desconhecido"
            txtPostContent.text = it.description
            txtPostDate.text = DateFormat.getDateFormat(requireContext()).format(it.date ?: Date())

            if (it.picture.isNotEmpty()){
                val decodedString = Base64.decode(it.picture[0], Base64.DEFAULT)
                val image = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)

                imgPost.setImageBitmap(image)
            } else {
                val resource = when(it.type){
                    AdType.PET_LOST -> R.drawable.ic_pet_lost
                    AdType.PET_DONATION -> R.drawable.ic_pet_donation
                    AdType.PET_ADVERTISEMENT -> R.drawable.ic_cao
                    else -> R.drawable.ic_cao
                }

                imgPost.setImageResource(resource)
            }



            chatID = it.chat

            chatViewModel.loadMessages(chatID)
            vm.updateResolveStatus()
        })
    }
}