function checkingPassword(){
    const password = document.getElementById("password").value;
    const repeatPassword = document.getElementById("repeatPassword").value;

    const buttonReg = document.getElementById("buttonReg");
    buttonReg.disabled = !(password === repeatPassword && password != null && repeatPassword != null);
}