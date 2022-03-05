const form = document.getElementById("form");

form.addEventListener("submit", function (event) {
  event.preventDefault();
  const button = form.querySelector("button");
  button.classList.add("loading");
  button.disabled = true;
});