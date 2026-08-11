import { useEffect, useState } from "react";
import { Link, useSearchParams } from "react-router-dom";
import { toast } from "react-toastify";

import Navbar from "../components/Navbar";

import {
  getAllPosts,
  searchPosts,
  getPostsByCategory,
  getImageUrl,
} from "../api/postApi";

import { getUserImageUrl, getAllUsers } from "../api/userApi";

import {
  likePost,
  unlikePost,
  getLikeCount,
  hasUserLiked,
} from "../api/likeApi";

import { decodeToken } from "../utils/jwt";

function Home() {
  const [posts, setPosts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const [keyword, setKeyword] = useState("");
  const [searching, setSearching] = useState(false);

  const [searchParams] = useSearchParams();
  const categoryId = searchParams.get("category");

  const [currentUserId, setCurrentUserId] = useState(null);

  const token = localStorage.getItem("token");

  useEffect(() => {
    loadCurrentUser();
  }, []);

  useEffect(() => {
    if (categoryId) {
      loadByCategory(categoryId);
    } else {
      loadAllPosts();
    }
  }, [categoryId]);

  // Reload likes after current user is identified
  useEffect(() => {
    if (currentUserId) {
      if (categoryId) {
        loadByCategory(categoryId);
      } else {
        loadAllPosts();
      }
    }
  }, [currentUserId]);

  async function loadCurrentUser() {
    try {
      const email = decodeToken(token)?.sub;

      const res = await getAllUsers();

      const me = res.data.find(
        (u) => u.email === email
      );

      if (me) {
        setCurrentUserId(me.id);
      }
    } catch (err) {
      console.log("Couldn't identify current user.", err);
    }
  }

  async function enrichPosts(list) {
    const updated = await Promise.all(
      list.map(async (post) => {
        try {
          const countResponse = await getLikeCount(post.postId);

          let liked = false;

          if (currentUserId) {
            const likedResponse = await hasUserLiked(
              post.postId,
              currentUserId
            );

            liked = likedResponse.data;
          }

          return {
            ...post,
            likeCount: countResponse.data,
            liked,
          };
        } catch (err) {
          console.log(
            "Like information couldn't be loaded for post:",
            post.postId
          );

          return {
            ...post,
            likeCount: 0,
            liked: false,
          };
        }
      })
    );

    setPosts(updated);
  }

  async function loadAllPosts() {
    setLoading(true);
    setSearching(false);
    setError("");

    try {
      const response = await getAllPosts(0);

      await enrichPosts(response.data.content || []);
    } catch (err) {
      console.log(err);
      setError("Couldn't load posts.");
    } finally {
      setLoading(false);
    }
  }

  async function loadByCategory(id) {
    setLoading(true);
    setSearching(false);
    setError("");

    try {
      const response = await getPostsByCategory(id);

      await enrichPosts(response.data || []);
    } catch (err) {
      console.log(err);
      setError("Couldn't load category.");
    } finally {
      setLoading(false);
    }
  }

  async function handleSearch(e) {
    e.preventDefault();

    if (!keyword.trim()) {
      if (categoryId) {
        loadByCategory(categoryId);
      } else {
        loadAllPosts();
      }

      return;
    }

    setLoading(true);
    setSearching(true);
    setError("");

    try {
      const response = await searchPosts(keyword);

      await enrichPosts(response.data || []);
    } catch (err) {
      console.log(err);
      toast.error("Search failed.");
      setError("Search failed.");
    } finally {
      setLoading(false);
    }
  }

 async function handleLike(postId) {
  if (!currentUserId) return;

  const post = posts.find(
    (p) => p.postId === postId
  );

  if (!post) return;

  try {
    if (post.liked) {
      await unlikePost(postId, currentUserId);

      setPosts((currentPosts) =>
        currentPosts.map((p) =>
          p.postId === postId
            ? {
                ...p,
                liked: false,
                likeCount: Math.max(0, p.likeCount - 1),
              }
            : p
        )
      );

    } else {
      await likePost(postId, currentUserId);

      setPosts((currentPosts) =>
        currentPosts.map((p) =>
          p.postId === postId
            ? {
                ...p,
                liked: true,
                likeCount: p.likeCount + 1,
              }
            : p
        )
      );
    }

  } catch {
    toast.error("Couldn't update like.");
  }
}

  return (
    <>
      <Navbar />

      <div className="page">

        {/* SEARCH */}
        <form
          onSubmit={handleSearch}
          style={{
            display: "flex",
            gap: "0.5rem",
            margin: "1.5rem 0",
          }}
        >
          <input
            type="text"
            value={keyword}
            onChange={(e) => setKeyword(e.target.value)}
            placeholder="Search posts by title..."
            style={{ flex: 1 }}
          />

          <button
            className="btn-primary"
            type="submit"
          >
            Search
          </button>
        </form>

        {/* LOADING */}
        {loading && (
          <p className="empty-state">
            Loading...
          </p>
        )}

        {/* ERROR */}
        {error && (
          <p className="error-text">
            {error}
          </p>
        )}

        {/* EMPTY */}
        {!loading &&
          !error &&
          posts.length === 0 && (
            <p className="empty-state">
              {searching
                ? "No posts found."
                : "Nothing published yet."}
            </p>
          )}

        {/* POSTS */}
        {!loading &&
          !error &&
          posts.map((post) => (
            <article
              className="post-card home-post-card"
              key={post.postId}
            >

              {/* TITLE */}
              <h2 className="home-post-title">
                {post.title}
              </h2>

              {/* AUTHOR */}
              <div className="byline home-byline">

                <img
                  src={
                    post.user?.profileImage
                      ? getUserImageUrl(
                          post.user.profileImage
                        )
                      : "/default-avatar.png"
                  }
                  alt={post.user?.username || "User"}
                  className="home-avatar"
                />

                <div>
                  <div className="home-author">
                    {post.user?.username || "Unknown"}
                  </div>

                  <span className="badge">
                    {post.category?.categoryTitle ||
                      "Uncategorized"}
                  </span>
                </div>

              </div>

              {/* POST IMAGE */}
              {post.imageName && (
                <img
                  src={getImageUrl(post.imageName)}
                  alt={post.title}
                  className="home-post-image"
                />
              )}

              {/* CONTENT PREVIEW */}
              <p className="home-post-content">
                {post.content &&
                post.content.length > 140
                  ? `${post.content.substring(0, 140)}...`
                  : post.content}
              </p>

              {/* BOTTOM ACTIONS */}
              <div className="home-post-actions">

                {/* LIKE */}
                <button
                  type="button"
                  onClick={() =>
                    handleLike(post.postId)
                  }
                  className={`like-button ${
                    post.liked ? "liked" : ""
                  }`}
                  title={
                    post.liked
                      ? "Unlike"
                      : "Like"
                  }
                >
                  <span className="like-icon">
                    {post.liked ? "♥" : "♡"}
                  </span>

                  <span className="like-count">
                    {post.likeCount || 0}
                  </span>
                </button>

                {/* READ MORE */}
                <Link
                  className="btn btn-secondary read-more-btn"
                  to={`/posts/${post.postId}`}
                >
                  Read More
                </Link>

              </div>

            </article>
          ))}

      </div>
    </>
  );
}

export default Home;