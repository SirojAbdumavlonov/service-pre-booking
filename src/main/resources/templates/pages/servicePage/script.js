var countdownNumber = document.getElementById('countdown-number');
var countdown = setInterval(function() {
  if (countdownNumber.innerHTML > 0) {
    countdownNumber.innerHTML--;
  } else {
    clearInterval(countdown);
    document.getElementById('submit-button').disabled = true;
    document.getElementById('submit-button').value = 'Sold Out';
  }
}, 1000);

// Time button click event
var timeButtons = document.querySelectorAll('.time-button');
timeButtons.forEach(function(button) {
  button.addEventListener('click', function() {
    document.getElementById('selected-time').value = this.dataset.time;
    document.getElementById('submit-button').disabled = false;
  });
});

// Form submit event
document.getElementById('booking-form').addEventListener('submit', function(event) {
  event.preventDefault();
  alert('You have booked the service for ' + document.getElementById('selected-time').value);
});

var buttons = document.querySelectorAll('.time-button');

buttons.forEach(function(button) {
  button.addEventListener('click', function() {
    // Remove selected class from all buttons
    buttons.forEach(function(button) {
      button.classList.remove('selected');
    });
    // Add selected class to clicked button
    this.classList.add('selected');
  });
});

var buttons1 = document.querySelectorAll('.masters-button');

buttons1.forEach(function(button) {
  button.addEventListener('click', function() {
    // Remove selected class from all buttons
    buttons1.forEach(function(button) {
      button.classList.remove('selected');
    });
    // Add selected class to clicked button
    this.classList.add('selected');
  });
});

var weekDays = ['Sun', 'Mon', 'Tues', 'Wed', 'Thur', 'Fri', 'Sat'];
var buttons = document.getElementsByClassName('date');

for(let i = 0; i < buttons.length; i++){
  let today = new Date();
  today.setDate(today.getDate() + i);
  //today = today.toLocaleDateString();
  todayValue = today.toLocaleDateString();
  today = today.getDate() + ", " + weekDays[today.getDay()];
  buttons[i].value = todayValue;
  if(i === 0){
    buttons[i].textContent = "TODAY";
  }
  else if (i === 1) {
    buttons[i].textContent = "TOMORROW";
  }
  else{
  buttons[i].textContent = today;
  }
}
