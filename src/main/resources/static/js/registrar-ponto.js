function updateDateTime() {
    const now = new Date();
    const dateOptions = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };
    const dateString = now.toLocaleDateString('pt-BR', dateOptions);
    const timeString = now.toLocaleTimeString('pt-BR');

    document.querySelector('.date').textContent = dateString;
    document.getElementById('liveTime').textContent = timeString;
    document.getElementById('dataHora').value = now.toISOString(); // Atualiza o campo dataHora
}

updateDateTime();
setInterval(updateDateTime, 1000);