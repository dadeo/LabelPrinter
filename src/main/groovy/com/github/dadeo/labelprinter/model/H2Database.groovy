package com.github.dadeo.labelprinter.model

import groovy.sql.Sql

class H2Database {
    private int port
    private String user
    private String password
    private Sql sql
    private List<Label> labels = new LinkedList<>()

    H2Database save() {
        labels.each { saveOrUpdate(it) }
        this
    }

    H2Database saveOrUpdate(Label label) {
        if (exists(label))
            updateLabel(label)
        else
            insertLabel(label)
    }

    H2Database addLabel(Label person) {
        labels << person
        this
    }

    H2Database removeLabel(int index) {
        labels.remove(index)
        this
    }

    H2Database updateLabel(int index) {
        withConnection {
            Label label = labels[index]
            updateLabel(label)
        }
    }

    List<Label> getLabels() {
        labels.asImmutable()
    }

    H2Database insertLabel(Label label) {
        def result = sql.executeInsert(
            '''
                    insert into Labels (line1, line2, line3, line4, printed)
                    values (?, ?, ?, ?, FALSE),
                ''',
            [
                label.line1,
                label.line2,
                label.line3,
                label.line4,
            ])

        label.id = result[0][0]

        this
    }

    H2Database updateLabel(Label label) {
        sql.executeUpdate(
            '''
                    update Labels
                    set line1 = ?,
                        line2 = ?,
                        line3 = ?,
                        line4 = ?,
                        printed = ?
                    where id = ?
                ''',
            [
                label.line1,
                label.line2,
                label.line3,
                label.line4,
                label.printed,
                label.id
            ])

        this
    }

    List<Label> load() {
        labels.clear()

        sql.eachRow(
            'select * from Labels',
            { rs ->
                this.@labels << new Label(id: rs.id,
                                          line1: rs.line1,
                                          line2: rs.line2,
                                          line3: rs.line3,
                                          line4: rs.line4,
                                          printed: rs.printed)
            })

        getLabels()
    }

    void saveToFile(File file) {
    }

    void loadFromFile(File file) {
    }


    def <T> T withConnection(Closure<T> closure) {
        connect()
        try {
            closure()
        } finally {
            disconnect()
        }
    }

    void connect() {
        if (user == null || password == null || port == 0)
            throw new RuntimeException('Database must be configured() before trying to connect().')

        sql = Sql.newInstance(url: 'jdbc:h2:~/labels', user: user, password: password, driver: 'org.h2.Driver')

        createTablesIfNecessary()
    }

    void disconnect() {
        sql.close()
        sql = null
    }

    void dropTables() {
        try {
            sql.execute 'drop table Labels'
        } catch (e) {
            // eat it
        }
    }

    void createTablesIfNecessary() {
        sql.execute '''
            create table if not exists Labels (
                id INTEGER IDENTITY,
                line1 VARCHAR(125),
                line2 VARCHAR(125),
                line3 VARCHAR(125),
                line4 VARCHAR(125),
                printed BIT
            )
        '''
    }

    boolean exists(Label label) {
        boolean found = false
        sql.eachRow('select * from Labels where id = :id', id: label.id, { found = true })
        found
    }

    void configure(int port, String user, String password) {
        this.port = port
        this.user = user
        this.password = password
    }
}