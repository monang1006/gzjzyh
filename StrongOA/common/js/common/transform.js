function transForSpecialSign(str)   
{   
 var result = '';   
 for (i=0 ; i<str.length; i++)   
 {   
  code = str.charCodeAt(i);//获取当前字符的Unicode编码   
  if ((code >= 33 && code <= 47) || (code >= 58 && code <= 64) || (code >= 91 && code <= 96) || (code >= 123 && code <= 126))//在这个Unicode编码范围中的是所有的半角符号   
  {   
   result += String.fromCharCode(str.charCodeAt(i) + 65248);//把半角字符的Unicode编码转换为对应全角字符的Unicode码   
  }else  
  {   
   result += str.charAt(i);   
  }   
 }   
 return result;   
}