import { useEffect, useState } from "react";
import { useParams, useNavigate, Link } from "react-router-dom";
import { toast } from "react-toastify";

import { createComment, deleteComment } from "../api/commentApi";
import { decodeToken } from "../utils/jwt";
import {
  getPostById,
  deletePost,
  uploadPostImage,
  getImageUrl,
} from "../api/postApi";
import { getUserImageUrl } from "../api/userApi";

function PostDetail() {
  const { postId } = useParams();
  const navigate = useNavigate();

  const [post, setPost] = useState(null);
  const [loading, setLoading] = useState(true);

  const [newComment, setNewComment] = useState("");
  const [submitting, setSubmitting] = useState(false);
  const [uploading, setUploading] = useState(false);

  const token = localStorage.getItem("token");
  const myEmail = decodeToken(token)?.sub;

  const isAuthor = post?.user?.email === myEmail;

  useEffect(() => {
    loadPost();
  }, [postId]);

  async function loadPost() {
    try {
      const response = await getPostById(postId);
      setPost(response.data);
    } catch (err) {
      toast.error("Couldn't load this post.");
    } finally {
      setLoading(false);
    }
  }

  async function handleDeletePost() {
    if (!window.confirm("Delete this post? This can't be undone.")) return;

    try {
      await deletePost(postId);

      toast.success("Post deleted successfully!");

      setTimeout(() => {
        navigate("/");
      }, 800);

    } catch (err) {
      toast.error("Couldn't delete this post.");
    }
  }

  async function handleImageUpload(e) {
    const file = e.target.files[0];

    if (!file) return;

    setUploading(true);

    try {
      await uploadPostImage(postId, file);

      await loadPost();

      toast.success("Image uploaded successfully!");

    } catch (err) {
      toast.error("Couldn't upload image.");
    } finally {
      setUploading(false);
    }
  }

  async function handleAddComment(e) {
    e.preventDefault();

    if (!newComment.trim()) {
      toast.warning("Comment cannot be empty.");
      return;
    }

    setSubmitting(true);

    try {
      await createComment(postId, newComment);

      setNewComment("");

      await loadPost();

      toast.success("Comment added.");

    } catch (err) {
      toast.error("Couldn't add comment.");
    } finally {
      setSubmitting(false);
    }
  }

  async function handleDeleteComment(commentId) {
    if (!window.confirm("Delete this comment?")) return;

    try {
      await deleteComment(commentId);

      await loadPost();

      toast.success("Comment deleted.");

    } catch (err) {
      toast.error("Couldn't delete comment.");
    }
  }

  if (loading) {
    return (
      <div className="page">
        <h2>Loading...</h2>
      </div>
    );
  }

  if (!post) return null;

  return (
    <div className="page post-detail">

      <Link to="/" className="back-link">
        &larr; Back to all posts
      </Link>

      <h1 style={{ marginBottom: "1rem" }}>
        {post.title}
      </h1>

      <div
        style={{
          display: "flex",
          alignItems: "center",
          gap: "15px",
          marginBottom: "1.5rem",
        }}
      >
        <img
          src={
            post.user?.profileImage
              ? getUserImageUrl(post.user.profileImage)
              : "/default-avatar.png"
          }
          alt={post.user?.username}
          style={{
            width: "60px",
            height: "60px",
            borderRadius: "50%",
            objectFit: "cover",
            border: "2px solid #ddd",
          }}
        />

        <div>
          <h3 style={{ margin: 0 }}>
            {post.user?.username || "Unknown"}
          </h3>

          <p
            style={{
              margin: "4px 0",
              color: "#666",
            }}
          >
            {post.user?.about}
          </p>

          <span className="badge">
            {post.category?.categoryTitle || "Uncategorized"}
          </span>
        </div>
      </div>

      {post.imageName && (
        <img
          src={getImageUrl(post.imageName)}
          alt={post.title}
          className="post-detail-image"
        />
      )}

      {isAuthor && (
        <div
          className="card-actions"
          style={{ marginTop: "0.75rem" }}
        >
          <Link
            to={`/posts/${postId}/edit`}
            className="btn btn-warning"
          >
            Edit
          </Link>

          <button
            className="btn btn-danger"
            onClick={handleDeletePost}
          >
            Delete
          </button>

          <label
            className="btn btn-secondary"
            style={{
              cursor: "pointer",
              marginBottom: 0,
            }}
          >
            {uploading
              ? "Uploading..."
              : post.imageName
              ? "Change Image"
              : "Add Image"}

            <input
              type="file"
              accept="image/*"
              style={{ display: "none" }}
              onChange={handleImageUpload}
            />
          </label>
        </div>
      )}

     <p style={{ whiteSpace: "pre-wrap" }}>
    {post.content}
</p>

      <hr className="divider" />

      <div className="comments">

        <h2>
          Comments ({post.comments?.length || 0})
        </h2>

{post.comments && post.comments.length > 0 ? (
  post.comments.map((comment) => (
    <div
      className="comment-row"
      key={comment.id}
    >
      <div>
        <strong>
          {comment.username || "User"}
        </strong>

        <p>{comment.content}</p>
      </div>

      {comment.userEmail === myEmail && (
        <button
          className="btn-ghost"
          onClick={() =>
            handleDeleteComment(comment.id)
          }
        >
          Delete
        </button>
      )}
    </div>
  ))
) : (
  <p className="empty-state">
    No comments yet.
  </p>
)}

        <form
          onSubmit={handleAddComment}
          className="comment-form"
        >
          <input
            type="text"
            value={newComment}
            onChange={(e) =>
              setNewComment(e.target.value)
            }
            placeholder="Write a comment..."
            style={{ flex: 1 }}
          />

          <button
            className="btn-primary"
            type="submit"
            disabled={submitting}
          >
            {submitting ? "Posting..." : "Post"}
          </button>
        </form>

      </div>

    </div>
  );
}

export default PostDetail;