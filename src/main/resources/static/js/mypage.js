const phoneNumber = document.querySelector('input[name="phoneNumber"]').value;
const email = document.querySelector('input[name="email"]').value;
const address = document.querySelector('input[name="address"]').value;
var btnEdit = document.getElementById('btnEdit');
var btnInterfaceBottom = document.getElementById('btnInterfaceBottom');
var btnCancel = document.getElementById('btnCancel');
var btnConfirm = document.getElementById('btnConfirm');

function activateForm(){
  btnEdit.style.display = 'none';
  btnInterfaceBottom.style.display = 'initial';
  $('#email').removeAttr('disabled');
  $('#phoneNumber').removeAttr('disabled');
  $('#address').removeAttr('disabled');
}