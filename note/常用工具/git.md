# git

## 常用命令

git pull - 更新代码  
git push - 推送代码  
git merge - 合并冲突  
git rebase - 合并代码并以新代码树为基础  
git commit -m "" - 提交文件填写备注  
git add - 将文件加入git管理  
git check - 切换分支  
git clone - 下载代码  
git status - 查看代码变更  
git diff - 比较文件不同  
git reset --hard - 会退版本
git stash - 临时保存工作进度

配置用户信息

```shell
git config --global user.name "runoob"
git config --global user.email test@runoob.com
```

## 网络问题

优先使用ssh协议

```shell
git remote set-url origin git@github.com:仓库名.git
```

## 删除误提交的文件

将文件从git跟踪列表中删除，不会删除本地文件

```shell
git rm -r --cached .idea
```