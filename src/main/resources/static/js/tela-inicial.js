const baterPontoBtn = document.getElementById('baterPontoBtn');

if (baterPontoBtn) {
	baterPontoBtn.addEventListener('click', () => {
		const cpf = baterPontoBtn.dataset.cpf;
		console.log("CPF lido do data-cpf:", cpf);
		window.location.href = `/bater-ponto?cpf=${cpf}`;
	});
}