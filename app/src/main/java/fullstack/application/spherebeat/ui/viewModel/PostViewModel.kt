package fullstack.application.spherebeat.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fullstack.application.spherebeat.model.Post
import fullstack.application.spherebeat.dal.repository.PostRepository

class PostViewModel : ViewModel() {
    private val postRepository: PostRepository = PostRepository()
    val postList: LiveData<List<Post>> = postRepository.getAllPosts()

    private val _loadingState = MutableLiveData<LoadingState>()
    val loadingState: LiveData<LoadingState> get() = _loadingState

    enum class LoadingState {
        LOADING,
        NOT_LOADING
    }

    fun getPostById(postId: String): LiveData<Post> {
        return postRepository.getPostById(postId)
    }

    fun refresh() {
        _loadingState.value = LoadingState.LOADING
        postRepository.getAllPosts()
        _loadingState.value = LoadingState.NOT_LOADING
    }

    fun addPost(post: Post, callback: (Boolean) -> Unit) {
        _loadingState.value = LoadingState.LOADING
        postRepository.addPost(post) { success ->
            if (success) {
                _loadingState.postValue(LoadingState.NOT_LOADING)
            }
            callback(success)
        }
    }

    fun likePost(post: Post, userId: String?, callback: (Boolean) -> Unit) {
        postRepository.likePost(post, userId) { success ->
            callback(success)
        }
    }

    fun updatePost(post: Post, callback: (Boolean) -> Unit) {
        _loadingState.value = LoadingState.LOADING
        postRepository.updatePost(post) { success ->
            if (success) {
                _loadingState.postValue(LoadingState.NOT_LOADING)
            }
            callback(success)
        }
    }

    fun deletePost(post: Post, callback: (Boolean) -> Unit) {
        _loadingState.value = LoadingState.LOADING
        postRepository.deletePost(post) { success ->
            if (success) {
                _loadingState.postValue(LoadingState.NOT_LOADING)
            }
            callback(success)
        }
    }

    fun deletePostById(postId: String, callback: (Boolean) -> Unit) {
        _loadingState.value = LoadingState.LOADING
        postRepository.deletePostById(postId) { success ->
            if (success) {
                _loadingState.postValue(LoadingState.NOT_LOADING)
            }
            callback(success)
        }
    }
}