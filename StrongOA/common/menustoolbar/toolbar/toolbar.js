function showme(obj1, obj2)
{
 if (moving)
  return;
 moving = true;
 
 
 for(i=0;i<document.all.tags('td').length;i++)
  if (document.all.tags('td')[i].className.indexOf('headtd') == 0)
   document.all.tags('td')[i].className = 'headtd1';
 obj2.className = 'headtd2';
 moveme(obj1);
 setTimeout("iframetimes('"+obj2.id+"F"+"','"+obj2.srcf+"')",500);
 
}
function iframetimes(f1,f2){
if(document.getElementById(f1).src=="")
document.getElementById(f1).src=scriptroot+"/theme/theme!RefreshPrivilist.action?priviparent="+f2;
}
function moveme(obj)
{
 idnumber = parseInt(obj.id.substr(4));
 objtop = headHeight * (idnumber - 1);
 objbuttom = bodyHeight + headHeight * (idnumber - 2);
 currenttop = parseInt(obj.style.top);
 if (currenttop >= objbuttom)
 {
  countid = 1;
  for(i=0;i<document.all.tags('div').length;i++)
   if (document.all.tags('div')[i].id == 'item'+countid+'body')
   {
    obj = document.all.tags('div')[i];
    objtop = headHeight * (countid - 1);
    if (countid == idnumber)
    {
     moveup(obj,objtop,false);
     break;
    }
    else
     moveup(obj,objtop,true);
    countid++;
   }
 }
 else if ((currenttop <= objtop) && (idnumber < objcount))
 {
  idnumber++;
  countid = objcount;
  for(i=document.all.tags('div').length-1;i>=0;i--)
   if (document.all.tags('div')[i].id == 'item'+countid+'body')
   {
    obj = document.all.tags('div')[i];
    objbuttom = bodyHeight + headHeight * (countid - 2);
    if (countid == idnumber)
    {
     movedown(obj,objbuttom,false);
     break;
    }
    else
     movedown(obj,objbuttom,true);
    countid--;
   }
 }
}
function moveup(obj,objtop,ismove)
{
 currenttop = parseInt(obj.style.top);
 if (currenttop > objtop)
 {
  obj.style.top = currenttop - step;
  setTimeout('moveup('+obj.id+','+objtop+','+ismove+')',1)
  return;
 }
 moving = ismove;
}
function movedown(obj,objbuttom,ismove)
{
 currenttop = parseInt(obj.style.top);
 if (currenttop < objbuttom)
 {
  obj.style.top = currenttop + step;
  setTimeout('movedown('+obj.id+','+objbuttom+','+ismove+')',1)
  return;
 }
 moving = ismove;
}

function navigates(url,title){
				window.parent.parent.actions_container.personal_properties_toolbar.navigate(scriptroot+url,title);
}