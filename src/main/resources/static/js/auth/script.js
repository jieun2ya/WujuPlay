document.getElementById('registration-form').addEventListener('submit', function (e) {
    e.preventDefault();

    // Get form values
    const phone = document.getElementById('phone').value;
    const birthday = document.getElementById('birthday').value;
    const MBTI = document.getElementById('MBTI').value;
    const sports_career = document.getElementById('sports_career').value;
    const play_number = document.getElementById('play_number').value;
    const open_number = document.getElementById('open_number').value;

    // Display submitted data
    const resultDiv = document.getElementById('result');
    resultDiv.style.display = 'block';
    resultDiv.innerHTML = `
        <h3>Registration Details:</h3>
        <p><strong>Phone:</strong> ${phone}</p>
        <p><strong>Birthday:</strong> ${birthday}</p>
        <p><strong>MBTI:</strong> ${MBTI}</p>
        <p><strong>Sports Career:</strong> ${sports_career}</p>
        <p><strong>Play Number:</strong> ${play_number}</p>
        <p><strong>Open Number:</strong> ${open_number}</p>
    `;
});
