document.getElementById('cpf').addEventListener('input', function (e) {
    let value = e.target.value.replace(/\D/g, '');
    let formattedValue = '';

    if (value.length > 0) {
        formattedValue = value.substring(0, 3);
        if (value.length > 3) {
            formattedValue += '.' + value.substring(3, 6);
        }
        if (value.length > 6) {
            formattedValue += '.' + value.substring(6, 9);
        }
        if (value.length > 9) {
            formattedValue += '-' + value.substring(9, 11);
        }
    }

    e.target.value = formattedValue;

    if (value.length > 11) {
        e.target.value = formattedValue.substring(0, 14);
    }
});