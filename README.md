# 基于 Spring Boot 的企业财务报销系统的设计与实现

本科毕业设计，作者：杨博川。采用前后端分离架构，实现企业报销申请、审批、凭证管理和财务统计等流程。

## 功能

- 用户登录、用户管理、角色管理与密码修改。
- 报销申请、报销类型、审批记录与审批处理。
- 财务凭证管理和报表统计。
- 公告、操作日志和文件上传。
- 按管理员、财务人员、员工角色展示菜单。

## 技术栈

| 部分 | 技术 |
| --- | --- |
| 后端 | Java 8、Spring Boot 2.7.5、MyBatis 2.2.2、Maven |
| 数据库 | MySQL 8 |
| 前端 | Vue 2.7、Vue Router 3、Vuex 3、Element UI 2、Axios |
| 构建 | Vite 3 |

## 目录

```text
backend/       Spring Boot 后端与 Maven 配置
frontend/      Vue 前端与依赖锁文件
database/      仅包含表结构的 SQL
```

## 本地运行

### 1. 初始化数据库

准备 MySQL 8，在 MySQL 客户端中执行 `database/schema.sql`。脚本创建 `finance_manage` 数据库和八张业务表，不包含原归档中的用户、报销、日志或附件数据。

新数据库为空。可在自己的本地数据库中初始化角色，并手动创建首个管理员；以下仅为示例，请自行替换密码：

```sql
USE finance_manage;
INSERT INTO sys_role (id, role_name, role_code) VALUES
  (1, '管理员', 'ADMIN'),
  (2, '财务人员', 'FINANCE'),
  (3, '员工', 'EMPLOYEE');
INSERT INTO sys_user (username, password, real_name, role_id, status)
VALUES ('local_admin', 'REPLACE_WITH_YOUR_LOCAL_PASSWORD', '本地演示管理员', 1, 1);
```

原前端使用角色 ID 1、2、3 区分菜单，因此初始化时应保持对应关系。

### 2. 启动后端

安装 JDK 8 和 Maven。后端配置已改为读取环境变量，原数据库密码未包含在仓库中。

Windows PowerShell 示例：

```powershell
$env:DB_USERNAME = 'root'
$env:DB_PASSWORD = '填写你自己的数据库密码'
# 可选：DB_URL、UPLOAD_PATH、UPLOAD_URL
cd backend
mvn spring-boot:run
```

默认服务地址为 `http://localhost:8080`。上传文件默认保存在后端运行目录下的 `uploads` 文件夹中。

### 3. 启动前端

在另一个终端运行：

```powershell
cd frontend
npm ci
npm run dev
```

在浏览器打开终端显示的 Vite 地址。前端当前请求本地 `http://localhost:8080`；如需更改后端地址，请同时检查 `frontend/src/main.js` 和报销申请页面中的上传地址。

前端构建命令：`npm run build`。

## 归档说明

本仓库从题为“2106110224杨博川基于Spring Boot的企业财务报销系统的设计与实现”的 2025 年 6 月毕业设计归档中整理，归档内部分源码修改于 2025 年 5 月。

整理时保留业务源码；将数据库连接信息和上传路径改为环境变量；从原 SQL 导出中提取表结构，去除原业务记录。论文、微信文件、上传附件、数据库数据、依赖目录、编译产物和 IDE 配置不包含在仓库中。原始归档未修改。

这是毕业设计源码归档，保留原有演示登录逻辑，未做生产环境安全改造。本次整理未验证完整数据库和前后端运行流程。
