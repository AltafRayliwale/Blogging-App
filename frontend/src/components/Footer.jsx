import "./Footer.css";

function Footer() {
  return (
    <footer className="footer">
      <div className="footer-container">

        <div className="footer-left">
          <h2>Blogging App</h2>
          <p>
            A modern blogging platform built with
            <strong> Spring Boot</strong> and <strong>React</strong>.
          </p>
        </div>

        <div className="footer-right">
          <h4>Developer</h4>

          <p>Altaf Rayliwale</p>

          <a
            href="https://github.com/YOUR_GITHUB"
            target="_blank"
            rel="noreferrer"
          >
            GitHub
          </a>

          <a
            href="https://linkedin.com/in/YOUR_LINKEDIN"
            target="_blank"
            rel="noreferrer"
          >
            LinkedIn
          </a>
        </div>

      </div>

      <hr />

      <p className="copyright">
        © 2026 Blogging App. All rights reserved.
      </p>
    </footer>
  );
}

export default Footer;