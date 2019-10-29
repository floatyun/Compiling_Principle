# Lexer
PL/0语言的简单词法分析程序的作业。
## WordType
关于WordType的具体构成见下表。  

| Type  | 实际所指的字符串  |  
|:---|:---|   
| Program  | program  |  
| ConstDeclare  | const  |  
| VarDeclare  | var  |  
| Procedure  | procedure  |  
| Begin  | begin  |  
| End  | end  |  
| If  | if  |  
| Then  | then  |  
| Else  | else  |  
| While  | while  |  
| Do  | do  |  
| Call  | call  |  
| Read  | read  |  
| Write  | write  |  
| Odd | odd |
| Assign  | :=  |  
| Plus  | +  |  
| Minus  | -  |  
| Mlt  | *  |  
| Div  | /  |  
| Greater  | >  |  
| Less  | <  |  
| Equal  | =  |  
| NotEqual  | <>  |  
| GreaterOrEqual  | >=  |  
| LessOrEqual  | <=  |  
| LPar  | (  |  
| RPar  | )  |  
| Comma  | ,  |  
| Id | 首字符是字母，接任意个字母或者数字的字符串，即正则表达式"[[A-Za-z_][A-Za-z0-9_]*]"|
| Integer | 一个或多个数字，即正则表达式"[\d]+"|
## 词法分析结果
词法分析结果是返回一个二元组(WordType,value)或者抛出一个错误LexException。  
二元组中的value：对于Integer，将直接使用计算机内部的int存储；对于Id，则是符号表中的下标；对于其它类型，则value无定义（不重要）。  
整个词法分析，返回的是一个结果（二元组或者错误）的线性表。  
## 输出
对于结果的输出，对于Id，采用"$"+Id+value；对于Integer，输出"$"+value;对于其它，则直接是Type名前加上'$',如$Program.
