#!/bin/bash

# =================================================
# Script de Configuração do WildFly para BIP API
# =================================================

echo "🚀 === CONFIGURAÇÃO WILDFLY PARA BIP API ==="
echo "Data: $(date)"
echo ""

WILDFLY_HOME=${WILDFLY_HOME:-/opt/wildfly}
JBOSS_CLI="$WILDFLY_HOME/bin/jboss-cli.sh"

if [ ! -f "$JBOSS_CLI" ]; then
    echo "❌ WildFly não encontrado em: $WILDFLY_HOME"
    echo "   Configure a variável WILDFLY_HOME ou instale o WildFly"
    exit 1
fi

echo "✅ WildFly encontrado: $WILDFLY_HOME"
echo ""

# Iniciar WildFly se não estiver rodando
echo "1. Verificando status do WildFly..."
if ! pgrep -f "wildfly" > /dev/null; then
    echo "   Iniciando WildFly..."
    $WILDFLY_HOME/bin/standalone.sh --server-config=standalone-full.xml &
    echo "   Aguardando inicialização..."
    sleep 15
else
    echo "   ✅ WildFly já está rodando"
fi

echo ""
echo "2. Configurando DataSource H2..."

# Configurar DataSource H2
$JBOSS_CLI --connect --commands="
/subsystem=datasources/data-source=BipDS:add(
    jndi-name=java:jboss/datasources/BipDS,
    driver-name=h2,
    connection-url=jdbc:h2:mem:bipdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE,
    user-name=sa,
    password=,
    min-pool-size=5,
    max-pool-size=20,
    pool-prefill=true,
    transaction-isolation=TRANSACTION_READ_COMMITTED,
    enabled=true
)"

if [ $? -eq 0 ]; then
    echo "   ✅ DataSource BipDS criado com sucesso"
else
    echo "   ⚠️ DataSource pode já existir (ignorando erro)"
fi

echo ""
echo "3. Configurando Logging..."

# Configurar logging para aplicação
$JBOSS_CLI --connect --commands="
/subsystem=logging/logger=com.bip:add(level=INFO),
/subsystem=logging/logger=com.bip.ejb:add(level=DEBUG)
"

echo "   ✅ Logging configurado"

echo ""
echo "4. Testando DataSource..."

# Testar conexão do DataSource
$JBOSS_CLI --connect --command="/subsystem=datasources/data-source=BipDS:test-connection-in-pool"

if [ $? -eq 0 ]; then
    echo "   ✅ DataSource testado com sucesso"
else
    echo "   ❌ Erro ao testar DataSource"
fi

echo ""
echo "5. Configurações aplicadas:"
echo "   - DataSource: java:jboss/datasources/BipDS"
echo "   - Banco: H2 em memória"
echo "   - Pool: 5-20 conexões"
echo "   - Logging: INFO para com.bip"
echo ""

echo "🎯 === PRÓXIMOS PASSOS ==="
echo "1. Compilar aplicação:"
echo "   cd jakarta-ee && mvn clean package"
echo ""
echo "2. Deploy da aplicação:"
echo "   mvn wildfly:deploy"
echo ""
echo "3. Ou deploy manual:"
echo "   cp target/bip-api-jakarta-ee.war $WILDFLY_HOME/standalone/deployments/"
echo ""
echo "4. Acessar aplicação:"
echo "   http://localhost:8080/bip-api-jakarta-ee/"
echo ""
echo "5. Management Console:"
echo "   http://localhost:9990"
echo ""

echo "✅ Configuração concluída!"