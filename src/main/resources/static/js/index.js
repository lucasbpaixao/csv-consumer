let buttonUpload = document.getElementById("upload");
let alerts = document.getElementById("alerts");

buttonUpload.addEventListener("click", uploadFunction)

function uploadFunction(){
    let inputFile = document.getElementById("inputFile");
    let pk = document.getElementById("pk");
    let tableName = document.getElementById("tableName");
	let files = inputFile.files;
    let formData = new FormData();
	formData.append("csvFile", files[0]);
    formData.append("tableName", tableName.value);
    formData.append("primaryKeyField", pk.value)
    let xhr = new XMLHttpRequest();
	xhr.open("POST", window.location.href + "upload", true);
						
	xhr.onreadystatechange = () => {

        if (xhr.readyState == 4) {

            if (xhr.status == 200) {
                showAlert("Upload realizado com sucesso!!!","success");
            } else {
                showAlert(JSON.parse(xhr.responseText).message, "danger");
            }
        }
	};
						
	xhr.send(formData);	
}