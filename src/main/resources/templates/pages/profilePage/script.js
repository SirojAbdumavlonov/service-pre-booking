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
