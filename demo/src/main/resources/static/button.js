

const btn = document.querySelector('form');
const btnText = document.querySelector('.btn .btn-text');
const btnIcon = document.querySelector('.btn .btn-icon');

btn.addEventListener('submit', () => {
	btn.classList.add('sending');
	btnText.innerHTML = 'Calculating...';

	setTimeout(() => {
		btn.classList.remove('sending');
		btnText.innerHTML = '<i class="fas fa-check"></i>';
		btn.classList.add('sent');
	}, 5000);
});
