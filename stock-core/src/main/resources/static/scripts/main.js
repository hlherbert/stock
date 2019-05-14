let myHeading = document.querySelector('h1');
myHeading.textContent = "Hello World!";
let html = document.querySelector('html');
html.onclick = function() {
 //   alert('别错我，痛！');
}
let myImg = document.querySelector('img');

myImg.onclick = function() {
    let mySrc = myImg.getAttribute('src');
    if (mySrc === 'images/background.jpg') {
        myImg.setAttribute('src','images/firefox.jpg');
    } else {
        myImg.setAttribute('src','images/background.jpg');
    }
}

function setHeading(name) {
    let myHeading = document.querySelector('h1');
    myHeading.textContent = 'Mozilla酷毙啦!'+ name;
}

function setUserName() {
    let myName = prompt('请输入你的名字!');
    localStorage.setItem('name', myName);
    setHeading(myName);
}

let storedName = localStorage.getItem('name');
if (!storedName) {
    setUserName();
} else {
    setHeading(storedName);
}

let myButton = document.querySelector('button');
myButton.onclick = setUserName;