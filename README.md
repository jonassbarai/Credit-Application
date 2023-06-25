# Credit-Application
API para Sistema de Avaliação de Créditos
PROJETO DA DIO.ME 

Cliente (Customer):
Cadastrar:
Request: firstName, lastName, cpf, income, email, password, zipCode e street
Response: String
Editar cadastro:
Request: id, firstName, lastName, income, zipCode, street
Response: firstName, lastName, income, cpf, email, income, zipCode, street
Visualizar perfil:
Request: id
Response: firstName, lastName, income, cpf, email, income, zipCode, street
Deletar cadastro:
Request: id
Response: sem retorno
Solicitação de Empréstimo (Credit):
Cadastrar:
Request: creditValue, dayFirstOfInstallment, numberOfInstallments e customerId
Response: String
Listar todas as solicitações de emprestimo de um cliente:
Request: customerId
Response: creditCode, creditValue, numberOfInstallment
Visualizar um emprestimo:
Request: customerId e creditCode
Response: creditCode, creditValue, numberOfInstallment, status, emailCustomer e incomeCustomer
API para Sistema de Avaliação de Créditos

Arquitetura em 3 camadas Projeto Spring Boot
