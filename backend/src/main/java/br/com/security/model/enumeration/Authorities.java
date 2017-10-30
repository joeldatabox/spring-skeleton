package br.com.security.model.enumeration;

/**
 * @author Joel Rodrigues Moreira on 20/10/17.
 * @project skeleton
 */
public enum Authorities {
    ROLE_USER {
        @Override
        public String getDescription() {
            return "ROLE_USER";
        }
    },
    ROLE_ADMIN {
        @Override
        public String getDescription() {
            return "ROLE_ADMIN";
        }
    },

    //<editor-fold desc="Contas">
    RoleContaCreate {
        @Override
        public String getDescription() {
            return "criar contas";
        }
    },
    RoleContaRead {
        @Override
        public String getDescription() {
            return "listar contas";
        }
    },
    RoleContaUpdate {
        @Override
        public String getDescription() {
            return "editar contas";
        }
    },
    RoleContaDelete {
        @Override
        public String getDescription() {
            return "deletar contas";
        }
    },
    //</editor-fold>

    //<editor-fold desc="Categoria de despesa">
    RoleCategoriaDespesaCreate {
        @Override
        public String getDescription() {
            return "criar categorias de despesa";
        }
    },
    RoleCategoriaDespesaRead {
        @Override
        public String getDescription() {
            return "listar categorias de despesa";
        }
    },
    RoleCategoriaDespesaUpdate {
        @Override
        public String getDescription() {
            return "editar categorias de despesa";
        }
    },
    RoleCategoriaDespesaDelete {
        @Override
        public String getDescription() {
            return "deletar categorias de despesa";
        }
    },
    //</editor-fold>

    //<editor-fold desc="Tipo de despesa">
    RoleTipoDespesaCreate {
        @Override
        public String getDescription() {
            return "criar tipos de despesa";
        }
    },
    RoleTipoDespesaRead {
        @Override
        public String getDescription() {
            return "listar tipos de despesa";
        }
    },
    RoleTipoDespesaUpdate {
        @Override
        public String getDescription() {
            return "editar tipos de despesa";
        }
    },
    RoleTipoDespesaDelete {
        @Override
        public String getDescription() {
            return "deletar tipos de despesa";
        }
    },
    //</editor-fold>

    //<editor-fold desc="Categoria de receita">
    RoleCategoriaReceitaCreate {
        @Override
        public String getDescription() {
            return "criar categorias de receita";
        }
    },
    RoleCategoriaReceitaRead {
        @Override
        public String getDescription() {
            return "listar categorias de receita";
        }
    },
    RoleCategoriaReceitaUpdate {
        @Override
        public String getDescription() {
            return "editar categorias de receita";
        }
    },
    RoleCategoriaReceitaDelete {
        @Override
        public String getDescription() {
            return "deletar categorias de receita";
        }
    };
    //</editor-fold>

    public abstract String getDescription();
}
