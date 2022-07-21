//商品展示js

var xmlHttpRequest;
var name;

function createXMLHttpRequest()
{
    xmlHttpRequest = new XMLHttpRequest();
}

function showInform(categoryId)
{
    name  = categoryId;
    var url = "/catalog/categoryShowJs?categoryId="+categoryId;
    sendRequest(url);
}

function sendRequest(url)
{
    createXMLHttpRequest();
    xmlHttpRequest.onreadystatechange = processInform;
    xmlHttpRequest.open("GET",url,true);
    xmlHttpRequest.send(null);

}
function processInform()
{
    if (xmlHttpRequest.readyState ==4)
        if(xmlHttpRequest.status ==200)
        {
            var rep = xmlHttpRequest.responseText;
            var inform = document.getElementById('inform');
            inform.innerText = rep;
            inform.className = name;
            inform.style.display = 'block';
        }
}

function hiddenInform(event)
{
    var informDiv = document.getElementById('inform');
    var x = event.clientX;
    var y = event.clientY;
    var divx1 = informDiv.offsetLeft;
    var divy1 = informDiv.offsetTop;
    var divx2 = informDiv.offsetLeft+informDiv.offsetWidth;
    var divy2 = informDiv.offsetTop+informDiv.offsetHeight;
    if(x < divx1 || x > divx2 || y < divy1 || y> divy2)
    {
        document.getElementById('inform').style.display = 'none';
    }
}

