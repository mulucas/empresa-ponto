function atualizarDataHora() {
    const now = new Date();
    const dataOpcoes = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric', timeZone: 'America/Fortaleza' };
    const horaOpcoes = { hour: 'numeric', minute: 'numeric', second: 'numeric', timeZone: 'America/Fortaleza' };

    document.querySelector('.date').textContent = now.toLocaleDateString('pt-BR', dataOpcoes);
    document.querySelector('#liveTime').textContent = now.toLocaleTimeString('pt-BR', horaOpcoes);
}

atualizarDataHora();
setInterval(atualizarDataHora, 1000);