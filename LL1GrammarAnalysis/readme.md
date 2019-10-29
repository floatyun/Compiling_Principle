# LL1GRammarAnalysis是第四章自顶向下分析的LL1文法的实验实现
## Products.txt
Products.txt记录要实现的文法的产生式。  
里面的终结符和非终结符都用'$'开头。其中终结符是词法分析里面给出的WordType。  
也就是说，这里把词法分析结果里的一个单词当作一个输入字母了。例如源代码中的"procedure"经过词法分析已经变成了一个整体的终结符("输入字母")——$Program.  
具体的终结符见词法分析的[README.md](../lex/README.md)。  
