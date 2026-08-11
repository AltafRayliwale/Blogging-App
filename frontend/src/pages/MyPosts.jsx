import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import Navbar from "../components/Navbar";
import { useAuth } from "../context/AuthContext";
import {
  getPostsByUser,
  deletePost,
  getImageUrl,
} from "../api/postApi";

function MyPosts() {
  const { currentUser } = useAuth();

  const [posts, setPosts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    if (currentUser) {
      loadPosts();
    }
  }, [currentUser]);

  async function loadPosts() {
    try {
      setLoading(true);
      const res = await getPostsByUser(currentUser.id);
      setPosts(res.data);
    } catch (err) {
      setError("Couldn't load your posts.");
    } finally {
      setLoading(false);
    }
  }

  async function handleDelete(postId) {
    const ok = window.confirm("Delete this post?");
    if (!ok) return;

    try {
      await deletePost(postId);
      loadPosts();
    } catch (err) {
      alert("Couldn't delete post.");
    }
  }

  return (
    <>
      <Navbar />

      <div className="page">

        <div
          style={{
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
            marginBottom: "30px",
          }}
        >
          <div>
            <h1>My Posts</h1>
            <p style={{ color: "#777" }}>
              Total Posts : <strong>{posts.length}</strong>
            </p>
          </div>

          <Link className="btn-primary" to="/create-post">
            + Create New Post
          </Link>
        </div>

        {loading && <p>Loading...</p>}

        {error && (
          <p className="error-text">{error}</p>
        )}

        {!loading && posts.length === 0 && (
          <div
            style={{
              textAlign: "center",
              padding: "60px 20px",
            }}
          >
            <h2>No Posts Yet</h2>
            <p>Create your first blog post.</p>

            <Link
              className="btn-primary"
              to="/create-post"
            >
              Create Post
            </Link>
          </div>
        )}

        <div className="posts-grid">

          {posts.map((post) => (

            <div
              className="post-card"
              key={post.postId}
            >
              {post.imageName && (
                <img
                  src={getImageUrl(post.imageName)}
                  alt={post.title}
                />
              )}

              <h2>{post.title}</h2>

              <div className="byline">
                <span className="badge">
                  {post.category?.categoryTitle}
                </span>
              </div>

              <p>
                {post.content.substring(0, 130)}...
              </p>

              <div className="card-actions">

                <Link
                  className="btn btn-secondary"
                  to={`/posts/${post.postId}`}
                >
                  View
                </Link>

                <Link
                  className="btn btn-warning"
                  to={`/posts/${post.postId}/edit`}
                >
                  Edit
                </Link>

                <button
                  className="btn btn-danger"
                  onClick={() =>
                    handleDelete(post.postId)
                  }
                >
                  Delete
                </button>

              </div>
            </div>

          ))}

        </div>

      </div>
    </>
  );
}

export default MyPosts;