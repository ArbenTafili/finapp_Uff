# -*- coding: utf-8 -*-
"""
Script de Geracao do Documento Consolidado do Projeto FinApp em PDF
Consolida todos os documentos tecnicos e de gestao das Rodadas 1, 2 e 3.
Gera graficos vetoriais SVG integrados (EAP, Gantt com prazos/atrasos, Curva S, Burndowns).
Converte para PDF executivo via Microsoft Edge Headless.
"""

import os
import subprocess
import markdown

BASE_DIR = os.path.dirname(os.path.abspath(__file__))
DOCS_DIR = os.path.join(BASE_DIR, "docs")
OUTPUT_HTML = os.path.join(BASE_DIR, "Documento_Consolidado_FinApp.html")
OUTPUT_PDF = os.path.join(BASE_DIR, "FinApp_Documento_Consolidado_Final.pdf")
OUTPUT_PDF_DOCS = os.path.join(DOCS_DIR, "FinApp_Documento_Consolidado_Final.pdf")

def read_doc(rel_path):
    full_path = os.path.join(DOCS_DIR, rel_path)
    if os.path.exists(full_path):
        with open(full_path, "r", encoding="utf-8") as f:
            return f.read()
    return ""

# Geradores de SVG de alta precisao e visual moderno

def svg_eap_diagram():
    return """
    <div class="diagram-box">
      <div class="diagram-title">Estrutura Analitica do Projeto (EAP / WBS) - Diagrama Hierarquico</div>
      <svg viewBox="0 0 1100 560" width="100%" height="auto" xmlns="http://www.w3.org/2000/svg">
        <defs>
          <linearGradient id="gradRoot" x1="0%" y1="0%" x2="100%" y2="100%">
            <stop offset="0%" stop-color="#1e3a8a" />
            <stop offset="100%" stop-color="#2563eb" />
          </linearGradient>
          <linearGradient id="gradN1" x1="0%" y1="0%" x2="100%" y2="100%">
            <stop offset="0%" stop-color="#047857" />
            <stop offset="100%" stop-color="#10b981" />
          </linearGradient>
          <linearGradient id="gradN2" x1="0%" y1="0%" x2="100%" y2="100%">
            <stop offset="0%" stop-color="#b45309" />
            <stop offset="100%" stop-color="#f59e0b" />
          </linearGradient>
          <linearGradient id="gradN3" x1="0%" y1="0%" x2="100%" y2="100%">
            <stop offset="0%" stop-color="#4338ca" />
            <stop offset="100%" stop-color="#6366f1" />
          </linearGradient>
          <linearGradient id="gradN4" x1="0%" y1="0%" x2="100%" y2="100%">
            <stop offset="0%" stop-color="#be185d" />
            <stop offset="100%" stop-color="#ec4899" />
          </linearGradient>
          <linearGradient id="gradN5" x1="0%" y1="0%" x2="100%" y2="100%">
            <stop offset="0%" stop-color="#0f766e" />
            <stop offset="100%" stop-color="#14b8a6" />
          </linearGradient>
          <filter id="shadow" x="-5%" y="-5%" width="110%" height="110%">
            <feDropShadow dx="0" dy="3" stdDeviation="3" flood-opacity="0.15" />
          </filter>
        </defs>

        <!-- Conectores principais -->
        <path d="M 550,55 L 550,85" stroke="#94a3b8" stroke-width="2.5" fill="none" />
        <path d="M 110,85 L 990,85" stroke="#94a3b8" stroke-width="2.5" fill="none" />
        <path d="M 110,85 L 110,115" stroke="#94a3b8" stroke-width="2.5" fill="none" />
        <path d="M 330,85 L 330,115" stroke="#94a3b8" stroke-width="2.5" fill="none" />
        <path d="M 550,85 L 550,115" stroke="#94a3b8" stroke-width="2.5" fill="none" />
        <path d="M 770,85 L 770,115" stroke="#94a3b8" stroke-width="2.5" fill="none" />
        <path d="M 990,85 L 990,115" stroke="#94a3b8" stroke-width="2.5" fill="none" />

        <!-- No Raiz -->
        <rect x="420" y="10" width="260" height="45" rx="8" fill="url(#gradRoot)" filter="url(#shadow)" />
        <text x="550" y="38" text-anchor="middle" font-family="'Segoe UI', Roboto, sans-serif" font-weight="700" font-size="16" fill="#ffffff">Projeto FinApp (Finanças)</text>

        <!-- Nivel 2: Macroetapas -->
        <!-- 1. Governanca -->
        <rect x="20" y="115" width="180" height="40" rx="6" fill="url(#gradN1)" filter="url(#shadow)" />
        <text x="110" y="140" text-anchor="middle" font-family="'Segoe UI', sans-serif" font-weight="600" font-size="13" fill="#ffffff">1.0 Governança &amp; Setup</text>

        <!-- 2. Arquitetura -->
        <rect x="240" y="115" width="180" height="40" rx="6" fill="url(#gradN2)" filter="url(#shadow)" />
        <text x="330" y="140" text-anchor="middle" font-family="'Segoe UI', sans-serif" font-weight="600" font-size="13" fill="#ffffff">2.0 Arquitetura &amp; Infra</text>

        <!-- 3. Requisitos Funcionais -->
        <rect x="460" y="115" width="180" height="40" rx="6" fill="url(#gradN3)" filter="url(#shadow)" />
        <text x="550" y="140" text-anchor="middle" font-family="'Segoe UI', sans-serif" font-weight="600" font-size="13" fill="#ffffff">3.0 Requisitos Funcionais</text>

        <!-- 4. Qualidade -->
        <rect x="680" y="115" width="180" height="40" rx="6" fill="url(#gradN4)" filter="url(#shadow)" />
        <text x="770" y="140" text-anchor="middle" font-family="'Segoe UI', sans-serif" font-weight="600" font-size="13" fill="#ffffff">4.0 Qualidade &amp; Testes</text>

        <!-- 5. Encerramento -->
        <rect x="900" y="115" width="180" height="40" rx="6" fill="url(#gradN5)" filter="url(#shadow)" />
        <text x="990" y="140" text-anchor="middle" font-family="'Segoe UI', sans-serif" font-weight="600" font-size="13" fill="#ffffff">5.0 Encerramento</text>

        <!-- Linhas e Folhas: Nivel 3 -->
        <!-- Coluna 1: Governanca -->
        <line x1="110" y1="155" x2="110" y2="480" stroke="#cbd5e1" stroke-width="2" />
        <g transform="translate(25, 175)" font-family="'Segoe UI', sans-serif" font-size="11" fill="#1e293b">
          <rect x="0" y="0" width="170" height="35" rx="4" fill="#ecfdf5" stroke="#a7f3d0" />
          <text x="85" y="22" text-anchor="middle">1.1 Repositório e CI/CD</text>
          <rect x="0" y="45" width="170" height="35" rx="4" fill="#ecfdf5" stroke="#a7f3d0" />
          <text x="85" y="67" text-anchor="middle">1.2 Backlog &amp; User Stories</text>
          <rect x="0" y="90" width="170" height="35" rx="4" fill="#ecfdf5" stroke="#a7f3d0" />
          <text x="85" y="112" text-anchor="middle">1.3 Planejamento &amp; Poker</text>
          <rect x="0" y="135" width="170" height="35" rx="4" fill="#ecfdf5" stroke="#a7f3d0" />
          <text x="85" y="157" text-anchor="middle">1.4 Modelagem de Classes</text>
          <rect x="0" y="180" width="170" height="35" rx="4" fill="#ecfdf5" stroke="#a7f3d0" />
          <text x="85" y="202" text-anchor="middle">1.5 Regras e Datas (ADR)</text>
          <rect x="0" y="225" width="170" height="35" rx="4" fill="#ecfdf5" stroke="#a7f3d0" />
          <text x="85" y="247" text-anchor="middle">1.6 Baseline de GP e EVM</text>
        </g>

        <!-- Coluna 2: Arquitetura -->
        <line x1="330" y1="155" x2="330" y2="440" stroke="#cbd5e1" stroke-width="2" />
        <g transform="translate(245, 175)" font-family="'Segoe UI', sans-serif" font-size="11" fill="#1e293b">
          <rect x="0" y="0" width="170" height="35" rx="4" fill="#fffbeb" stroke="#fde68a" />
          <text x="85" y="22" text-anchor="middle">2.1 Spring Boot / Kotlin</text>
          <rect x="0" y="45" width="170" height="35" rx="4" fill="#fffbeb" stroke="#fde68a" />
          <text x="85" y="67" text-anchor="middle">2.2 Docker &amp; Compose</text>
          <rect x="0" y="90" width="170" height="35" rx="4" fill="#fffbeb" stroke="#fde68a" />
          <text x="85" y="112" text-anchor="middle">2.3 PostgreSQL &amp; Flyway</text>
          <rect x="0" y="135" width="170" height="35" rx="4" fill="#fffbeb" stroke="#fde68a" />
          <text x="85" y="157" text-anchor="middle">2.4 Migração RDT-01</text>
          <rect x="0" y="180" width="170" height="35" rx="4" fill="#fffbeb" stroke="#fde68a" />
          <text x="85" y="202" text-anchor="middle">2.5 Deploy Cloud Render</text>
        </g>

        <!-- Coluna 3: Requisitos Funcionais -->
        <line x1="550" y1="155" x2="550" y2="440" stroke="#cbd5e1" stroke-width="2" />
        <g transform="translate(465, 175)" font-family="'Segoe UI', sans-serif" font-size="11" fill="#1e293b">
          <rect x="0" y="0" width="170" height="35" rx="4" fill="#eef2ff" stroke="#c7d2fe" />
          <text x="85" y="22" text-anchor="middle" font-weight="600">3.1 RF01 Transações</text>
          <rect x="0" y="45" width="170" height="35" rx="4" fill="#eef2ff" stroke="#c7d2fe" />
          <text x="85" y="67" text-anchor="middle" font-weight="600">3.2 RF02 Categorias</text>
          <rect x="0" y="90" width="170" height="35" rx="4" fill="#eef2ff" stroke="#c7d2fe" />
          <text x="85" y="112" text-anchor="middle" font-weight="600">3.3 RF03 Relatórios</text>
          <rect x="0" y="135" width="170" height="35" rx="4" fill="#eef2ff" stroke="#c7d2fe" />
          <text x="85" y="157" text-anchor="middle" font-weight="600">3.4 RF04 Metas Economia</text>
          <rect x="0" y="180" width="170" height="35" rx="4" fill="#eef2ff" stroke="#c7d2fe" />
          <text x="85" y="202" text-anchor="middle" font-weight="600">3.5 RF05 Exportação CSV</text>
        </g>

        <!-- Coluna 4: Qualidade -->
        <line x1="770" y1="155" x2="770" y2="440" stroke="#cbd5e1" stroke-width="2" />
        <g transform="translate(685, 175)" font-family="'Segoe UI', sans-serif" font-size="11" fill="#1e293b">
          <rect x="0" y="0" width="170" height="35" rx="4" fill="#fdf2f8" stroke="#fbcfe8" />
          <text x="85" y="22" text-anchor="middle">4.1 109 Testes Automat.</text>
          <rect x="0" y="45" width="170" height="35" rx="4" fill="#fdf2f8" stroke="#fbcfe8" />
          <text x="85" y="67" text-anchor="middle">4.2 Integração Testcontainers</text>
          <rect x="0" y="90" width="170" height="35" rx="4" fill="#fdf2f8" stroke="#fbcfe8" />
          <text x="85" y="112" text-anchor="middle">4.3 RNF02 Desempenho &lt; 3s</text>
          <rect x="0" y="135" width="170" height="35" rx="4" fill="#fdf2f8" stroke="#fbcfe8" />
          <text x="85" y="157" text-anchor="middle">4.4 RNF04 Cross-Browser</text>
          <rect x="0" y="180" width="170" height="35" rx="4" fill="#fdf2f8" stroke="#fbcfe8" />
          <text x="85" y="202" text-anchor="middle">4.5 RNF05 Precisão Moeda</text>
        </g>

        <!-- Coluna 5: Encerramento -->
        <line x1="990" y1="155" x2="990" y2="390" stroke="#cbd5e1" stroke-width="2" />
        <g transform="translate(905, 175)" font-family="'Segoe UI', sans-serif" font-size="11" fill="#1e293b">
          <rect x="0" y="0" width="170" height="35" rx="4" fill="#f0fdfa" stroke="#99f6e4" />
          <text x="85" y="22" text-anchor="middle">5.1 Demos v1, v2 e v3</text>
          <rect x="0" y="45" width="170" height="35" rx="4" fill="#f0fdfa" stroke="#99f6e4" />
          <text x="85" y="67" text-anchor="middle">5.2 Retrospectiva em Vídeo</text>
          <rect x="0" y="90" width="170" height="35" rx="4" fill="#f0fdfa" stroke="#99f6e4" />
          <text x="85" y="112" text-anchor="middle">5.3 Documento Consolidado</text>
        </g>
      </svg>
    </div>
    """

def svg_gantt_diagram():
    return """
    <div class="diagram-box">
      <div class="diagram-title">Cronograma Detalhado (Gantt) — Rastreamento de Prazo e Atrasos (Rodadas 1 a 3)</div>
      <svg viewBox="0 0 1100 620" width="100%" height="auto" xmlns="http://www.w3.org/2000/svg">
        <defs>
          <filter id="ganttShadow" x="-2%" y="-2%" width="104%" height="104%">
            <feDropShadow dx="0" dy="2" stdDeviation="2" flood-opacity="0.1" />
          </filter>
        </defs>

        <!-- Fundo e Grade de Datas -->
        <rect x="260" y="30" width="810" height="570" fill="#f8fafc" stroke="#e2e8f0" />
        
        <!-- Marcadores de Dias / Semanas -->
        <!-- 01/09 (x=260) a 28/10 (x=1070) -> 58 dias total = ~14px por dia -->
        <!-- R1: 01/09 a 14/09 (dias 0 a 13 -> x=260 a x=442) -->
        <!-- R2: 15/09 a 06/10 (dias 14 a 35 -> x=456 a x=750) -->
        <!-- R3: 07/10 a 28/10 (dias 36 a 57 -> x=764 a x=1058) -->

        <!-- Faixas verticais das rodadas -->
        <rect x="260" y="30" width="190" height="570" fill="#f0fdf4" opacity="0.6" />
        <rect x="450" y="30" width="300" height="570" fill="#fffbeb" opacity="0.6" />
        <rect x="750" y="30" width="320" height="570" fill="#eff6ff" opacity="0.6" />

        <!-- Cabecalho de datas -->
        <text x="355" y="22" text-anchor="middle" font-family="'Segoe UI', sans-serif" font-weight="700" font-size="12" fill="#15803d">RODADA 1 (01/09 a 14/09)</text>
        <text x="600" y="22" text-anchor="middle" font-family="'Segoe UI', sans-serif" font-weight="700" font-size="12" fill="#b45309">RODADA 2 (15/09 a 06/10)</text>
        <text x="910" y="22" text-anchor="middle" font-family="'Segoe UI', sans-serif" font-weight="700" font-size="12" fill="#1d4ed8">RODADA 3 (07/10 a 28/10)</text>

        <!-- Linhas e Tarefas -->
        <!-- Funcao helper de linha: y_pos, label, x, w, fill, status_label, status_color -->
        
        <!-- Grupo R1 -->
        <g font-family="'Segoe UI', sans-serif" font-size="11">
          <!-- T1 -->
          <text x="10" y="55" font-weight="600" fill="#334155">1.1 Setup Repo e CI/CD</text>
          <rect x="260" y="42" width="70" height="18" rx="3" fill="#10b981" filter="url(#ganttShadow)" />
          <text x="340" y="55" font-size="10" fill="#047857" font-weight="700">No Prazo</text>

          <!-- T2 -->
          <text x="10" y="80" font-weight="600" fill="#334155">1.2 Backlog &amp; User Stories</text>
          <rect x="260" y="67" width="85" height="18" rx="3" fill="#10b981" filter="url(#ganttShadow)" />
          <text x="355" y="80" font-size="10" fill="#047857" font-weight="700">No Prazo</text>

          <!-- T3 -->
          <text x="10" y="105" font-weight="600" fill="#334155">1.4 Modelagem de Classes</text>
          <rect x="330" y="92" width="100" height="18" rx="3" fill="#10b981" filter="url(#ganttShadow)" />
          <text x="440" y="105" font-size="10" fill="#047857" font-weight="700">No Prazo</text>

          <!-- T4 -->
          <text x="10" y="130" font-weight="600" fill="#334155">1.5 Regras e Datas Validadas</text>
          <rect x="345" y="117" width="100" height="18" rx="3" fill="#10b981" filter="url(#ganttShadow)" />
          <text x="455" y="130" font-size="10" fill="#047857" font-weight="700">No Prazo</text>

          <!-- M1 -->
          <text x="10" y="155" font-weight="700" fill="#0f766e">★ Demo v1 Homologada (14/09)</text>
          <polygon points="445,142 453,150 445,158 437,150" fill="#0d9488" />
          <line x1="445" y1="42" x2="445" y2="165" stroke="#0d9488" stroke-width="1.5" stroke-dasharray="3,3" />

          <!-- Separador R2 -->
          <line x1="0" y1="172" x2="1070" y2="172" stroke="#e2e8f0" stroke-width="1" />

          <!-- T5 -->
          <text x="10" y="195" font-weight="600" fill="#334155">3.1 Conclusão RF01 Transações</text>
          <rect x="455" y="182" width="130" height="18" rx="3" fill="#10b981" filter="url(#ganttShadow)" />
          <text x="595" y="195" font-size="10" fill="#047857" font-weight="700">No Prazo</text>

          <!-- T6 -->
          <text x="10" y="220" font-weight="600" fill="#334155">3.2 Conclusão RF02 Categorias</text>
          <rect x="525" y="207" width="115" height="18" rx="3" fill="#10b981" filter="url(#ganttShadow)" />
          <text x="650" y="220" font-size="10" fill="#047857" font-weight="700">No Prazo</text>

          <!-- T7 - ATRASO RDT-01 -->
          <text x="10" y="245" font-weight="700" fill="#b91c1c">2.2 Docker &amp; Postgres (RDT-01)</text>
          <rect x="495" y="232" width="160" height="18" rx="3" fill="#ef4444" filter="url(#ganttShadow)" />
          <text x="665" y="245" font-size="10" fill="#b91c1c" font-weight="700">Atraso RDT-01 (+8h)</text>

          <!-- T8 - ADIANTADO -->
          <text x="10" y="270" font-weight="700" fill="#15803d">3.3 Queries Agregadas RF03</text>
          <rect x="625" y="257" width="100" height="18" rx="3" fill="#3b82f6" filter="url(#ganttShadow)" />
          <text x="735" y="270" font-size="10" fill="#1d4ed8" font-weight="700">Adiantado (-2d)</text>

          <!-- T9 -->
          <text x="10" y="295" font-weight="600" fill="#334155">Governança Branches &amp; EVM R2</text>
          <rect x="610" y="282" width="125" height="18" rx="3" fill="#10b981" filter="url(#ganttShadow)" />
          <text x="745" y="295" font-size="10" fill="#047857" font-weight="700">No Prazo</text>

          <!-- M2 -->
          <text x="10" y="320" font-weight="700" fill="#b45309">★ Demo v2 Homologada (06/10)</text>
          <polygon points="745,307 753,315 745,323 737,315" fill="#f59e0b" />
          <line x1="745" y1="182" x2="745" y2="330" stroke="#f59e0b" stroke-width="1.5" stroke-dasharray="3,3" />

          <!-- Separador R3 -->
          <line x1="0" y1="337" x2="1070" y2="337" stroke="#e2e8f0" stroke-width="1" />

          <!-- T10 -->
          <text x="10" y="360" font-weight="600" fill="#334155">3.3 Interface Relatórios Web</text>
          <rect x="755" y="347" width="130" height="18" rx="3" fill="#10b981" filter="url(#ganttShadow)" />
          <text x="895" y="360" font-size="10" fill="#047857" font-weight="700">No Prazo</text>

          <!-- T11 -->
          <text x="10" y="385" font-weight="600" fill="#334155">3.4 Metas de Economia RF04</text>
          <rect x="795" y="372" width="165" height="18" rx="3" fill="#10b981" filter="url(#ganttShadow)" />
          <text x="970" y="385" font-size="10" fill="#047857" font-weight="700">No Prazo</text>

          <!-- T12 -->
          <text x="10" y="410" font-weight="600" fill="#334155">3.5 Exportação CSV RF05</text>
          <rect x="850" y="397" width="125" height="18" rx="3" fill="#10b981" filter="url(#ganttShadow)" />
          <text x="985" y="410" font-size="10" fill="#047857" font-weight="700">No Prazo</text>

          <!-- T13 -->
          <text x="10" y="435" font-weight="600" fill="#334155">3.2 Gestão de Categorias na UI</text>
          <rect x="825" y="422" width="115" height="18" rx="3" fill="#10b981" filter="url(#ganttShadow)" />
          <text x="950" y="435" font-size="10" fill="#047857" font-weight="700">No Prazo</text>

          <!-- T14 -->
          <text x="10" y="460" font-weight="600" fill="#334155">4.9 Deploy Render &amp; Conta Teste</text>
          <rect x="910" y="447" width="100" height="18" rx="3" fill="#10b981" filter="url(#ganttShadow)" />
          <text x="1020" y="460" font-size="10" fill="#047857" font-weight="700">No Prazo</text>

          <!-- T15 -->
          <text x="10" y="485" font-weight="600" fill="#334155">4.1 Suíte 109 Testes Automat.</text>
          <rect x="935" y="472" width="85" height="18" rx="3" fill="#10b981" filter="url(#ganttShadow)" />
          <text x="1030" y="485" font-size="10" fill="#047857" font-weight="700">No Prazo</text>

          <!-- T16 -->
          <text x="10" y="510" font-weight="600" fill="#334155">EVM Consolidado &amp; PDF Master</text>
          <rect x="990" y="497" width="60" height="18" rx="3" fill="#10b981" filter="url(#ganttShadow)" />
          <text x="990" y="527" font-size="10" fill="#047857" font-weight="700">No Prazo</text>

          <!-- T17 -->
          <text x="10" y="535" font-weight="600" fill="#334155">5.2 Retrospectiva Final Gravada</text>
          <rect x="1010" y="522" width="45" height="18" rx="3" fill="#10b981" filter="url(#ganttShadow)" />

          <!-- M3 -->
          <text x="10" y="565" font-weight="700" fill="#1d4ed8">★ Entrega Final &amp; Demo v3 (28/10)</text>
          <polygon points="1055,552 1063,560 1055,568 1047,560" fill="#2563eb" />
          <line x1="1055" y1="347" x2="1055" y2="575" stroke="#2563eb" stroke-width="2" stroke-dasharray="3,3" />
        </g>

        <!-- Legenda -->
        <g transform="translate(10, 585)" font-family="'Segoe UI', sans-serif" font-size="11">
          <rect x="0" y="0" width="15" height="12" rx="2" fill="#10b981" />
          <text x="22" y="10" fill="#334155">Entregue no Prazo</text>

          <rect x="150" y="0" width="15" height="12" rx="2" fill="#ef4444" />
          <text x="172" y="10" fill="#334155">Atraso RDT-01 (+8h / Absorvido)</text>

          <rect x="390" y="0" width="15" height="12" rx="2" fill="#3b82f6" />
          <text x="412" y="10" fill="#334155">Entrega Adiantada</text>

          <polygon points="560,0 568,6 560,12 552,6" fill="#f59e0b" />
          <text x="575" y="10" fill="#334155">Marco / Milestone Homologado</text>
        </g>
      </svg>
    </div>
    """

def svg_curva_s():
    return """
    <div class="diagram-box">
      <div class="diagram-title">Curva S — Valor Agregado Acumulado (PV vs. EV vs. AC ao Longo do Projeto)</div>
      <svg viewBox="0 0 950 420" width="100%" height="auto" xmlns="http://www.w3.org/2000/svg">
        <!-- Grade -->
        <rect x="80" y="30" width="820" height="320" fill="#fafafa" stroke="#e2e8f0" />
        <line x1="80" y1="110" x2="900" y2="110" stroke="#e2e8f0" stroke-dasharray="2,2" />
        <line x1="80" y1="190" x2="900" y2="190" stroke="#e2e8f0" stroke-dasharray="2,2" />
        <line x1="80" y1="270" x2="900" y2="270" stroke="#e2e8f0" stroke-dasharray="2,2" />

        <!-- Eixos -->
        <!-- Y: 0 a 700 h -> h=320 -> y = 350 - (val / 700 * 320) -->
        <text x="70" y="355" text-anchor="end" font-family="'Segoe UI', sans-serif" font-size="11" fill="#64748b">0 h</text>
        <text x="70" y="275" text-anchor="end" font-family="'Segoe UI', sans-serif" font-size="11" fill="#64748b">175 h</text>
        <text x="70" y="195" text-anchor="end" font-family="'Segoe UI', sans-serif" font-size="11" fill="#64748b">350 h</text>
        <text x="70" y="115" text-anchor="end" font-family="'Segoe UI', sans-serif" font-size="11" fill="#64748b">525 h</text>
        <text x="70" y="40" text-anchor="end" font-family="'Segoe UI', sans-serif" font-size="11" fill="#64748b">700 h</text>

        <!-- Marcos no Eixo X -->
        <!-- P0: x=80 (01/09) -->
        <!-- P1: x=244 (14/09 R1) -->
        <!-- P2: x=408 (25/09 R2-Meio) -->
        <!-- P3: x=572 (06/10 R2-Fim) -->
        <!-- P4: x=736 (17/10 R3-Meio) -->
        <!-- P5: x=900 (28/10 R3-Fim) -->
        <text x="80" y="375" text-anchor="middle" font-family="'Segoe UI', sans-serif" font-size="11" fill="#475569">01/09<br/>Início</text>
        <text x="244" y="375" text-anchor="middle" font-family="'Segoe UI', sans-serif" font-size="11" fill="#475569">14/09<br/>Fim R1</text>
        <text x="408" y="375" text-anchor="middle" font-family="'Segoe UI', sans-serif" font-size="11" fill="#475569">25/09<br/>Meio R2</text>
        <text x="572" y="375" text-anchor="middle" font-family="'Segoe UI', sans-serif" font-size="11" fill="#475569">06/10<br/>Fim R2</text>
        <text x="736" y="375" text-anchor="middle" font-family="'Segoe UI', sans-serif" font-size="11" fill="#475569">17/10<br/>Meio R3</text>
        <text x="900" y="375" text-anchor="middle" font-family="'Segoe UI', sans-serif" font-weight="700" font-size="11" fill="#1e3a8a">28/10<br/>Entrega Final</text>

        <!-- Linhas Curva S -->
        <!-- PV: [0, 120, 210, 300, 456, 612] -->
        <!-- y = 350 - val * (320/700) = 350 - val * 0.4571 -->
        <!-- 0->350 | 120->295 | 210->254 | 300->213 | 456->142 | 612->70 -->
        <polyline points="80,350 244,295 408,254 572,213 736,142 900,70" fill="none" stroke="#2563eb" stroke-width="3" stroke-dasharray="6,4" />

        <!-- EV: [0, 120, 205, 304, 465, 612] -->
        <!-- 0->350 | 120->295 | 205->256 | 304->211 | 465->137 | 612->70 -->
        <polyline points="80,350 244,295 408,256 572,211 736,137 900,70" fill="none" stroke="#16a34a" stroke-width="3.5" />

        <!-- AC: [0, 120, 218, 310, 470, 615] -->
        <!-- 0->350 | 120->295 | 218->250 | 310->208 | 470->135 | 615->69 -->
        <polyline points="80,350 244,295 408,250 572,208 736,135 900,69" fill="none" stroke="#dc2626" stroke-width="3" />

        <!-- Pontos nos marcos finais (x=900) -->
        <circle cx="900" cy="70" r="6" fill="#16a34a" />
        <circle cx="900" cy="69" r="4" fill="#dc2626" />
        <text x="890" y="55" font-family="'Segoe UI', sans-serif" font-weight="700" font-size="11" fill="#1e293b" text-anchor="end">EV: 612h (100%) | AC: 615h</text>

        <!-- Ponto do desvio na R2 (x=572) -->
        <circle cx="572" cy="208" r="5" fill="#dc2626" />
        <text x="560" y="195" font-family="'Segoe UI', sans-serif" font-size="10" fill="#b91c1c" text-anchor="end">Desvio Docker (AC > EV)</text>

        <!-- Legenda -->
        <g transform="translate(150, 400)" font-family="'Segoe UI', sans-serif" font-size="12">
          <line x1="0" y1="8" x2="30" y2="8" stroke="#2563eb" stroke-width="3" stroke-dasharray="6,4" />
          <text x="38" y="12" fill="#1e293b">PV (Valor Planejado - Baseline)</text>

          <line x1="260" y1="8" x2="290" y2="8" stroke="#16a34a" stroke-width="3.5" />
          <text x="298" y="12" fill="#1e293b">EV (Valor Agregado Entregue)</text>

          <line x1="520" y1="8" x2="550" y2="8" stroke="#dc2626" stroke-width="3" />
          <text x="558" y="12" fill="#1e293b">AC (Custo Real Incorrido)</text>
        </g>
      </svg>
    </div>
    """

def svg_burndowns():
    return """
    <div class="diagram-box">
      <div class="diagram-title">Gráficos de Burndown — Visão por Rodada e Consolidado Global</div>
      <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 20px; margin-top: 15px;">
        
        <!-- Burndown R1 -->
        <div style="background: #fafafa; border: 1px solid #e2e8f0; border-radius: 6px; padding: 10px;">
          <div style="font-weight: 700; font-size: 13px; color: #166534; text-align: center; margin-bottom: 5px;">Burndown Rodada 1 (120h)</div>
          <svg viewBox="0 0 400 200" width="100%" height="auto">
            <line x1="50" y1="20" x2="50" y2="160" stroke="#cbd5e1" />
            <line x1="50" y1="160" x2="380" y2="160" stroke="#cbd5e1" />
            <text x="45" y="25" text-anchor="end" font-size="10" fill="#64748b">120h</text>
            <text x="45" y="160" text-anchor="end" font-size="10" fill="#64748b">0h</text>
            <!-- Eixo X: 01/09, 03/09, 06/09, 10/09, 14/09 -->
            <!-- x: 50, 132, 215, 297, 380 -->
            <!-- Planejado: [120, 108, 96, 84, 0] -> y = 160 - (val/120 * 140) -->
            <!-- y: 20, 34, 48, 62, 160 -->
            <polyline points="50,20 132,34 215,48 297,62 380,160" fill="none" stroke="#94a3b8" stroke-width="2" stroke-dasharray="4,4" />
            <!-- Real: [120, 114, 98, 80, 0] -->
            <!-- y: 20, 27, 46, 67, 160 -->
            <polyline points="50,20 132,27 215,46 297,67 380,160" fill="none" stroke="#16a34a" stroke-width="2.5" />
            <circle cx="380" cy="160" r="4" fill="#16a34a" />
            <text x="50" y="175" text-anchor="middle" font-size="9" fill="#475569">01/09</text>
            <text x="215" y="175" text-anchor="middle" font-size="9" fill="#475569">06/09</text>
            <text x="380" y="175" text-anchor="middle" font-size="9" fill="#475569">14/09</text>
          </svg>
        </div>

        <!-- Burndown R2 -->
        <div style="background: #fafafa; border: 1px solid #e2e8f0; border-radius: 6px; padding: 10px;">
          <div style="font-weight: 700; font-size: 13px; color: #b45309; text-align: center; margin-bottom: 5px;">Burndown Rodada 2 (180h / 190h)</div>
          <svg viewBox="0 0 400 200" width="100%" height="auto">
            <line x1="50" y1="20" x2="50" y2="160" stroke="#cbd5e1" />
            <line x1="50" y1="160" x2="380" y2="160" stroke="#cbd5e1" />
            <text x="45" y="25" text-anchor="end" font-size="10" fill="#64748b">180h</text>
            <text x="45" y="160" text-anchor="end" font-size="10" fill="#64748b">0h</text>
            <!-- Planejado: [180, 140, 100, 60, 20, 0] -->
            <!-- y: 20, 51, 82, 113, 144, 160 -->
            <polyline points="50,20 116,51 182,82 248,113 314,144 380,160" fill="none" stroke="#94a3b8" stroke-width="2" stroke-dasharray="4,4" />
            <!-- Real: [180, 148, 105, 58, 18, 0] -->
            <!-- y: 20, 45, 78, 115, 146, 160 -->
            <polyline points="50,20 116,45 182,78 248,115 314,146 380,160" fill="none" stroke="#f59e0b" stroke-width="2.5" />
            <circle cx="380" cy="160" r="4" fill="#f59e0b" />
            <text x="50" y="175" text-anchor="middle" font-size="9" fill="#475569">15/09</text>
            <text x="182" y="175" text-anchor="middle" font-size="9" fill="#475569">25/09</text>
            <text x="380" y="175" text-anchor="middle" font-size="9" fill="#475569">06/10</text>
          </svg>
        </div>

        <!-- Burndown R3 -->
        <div style="background: #fafafa; border: 1px solid #e2e8f0; border-radius: 6px; padding: 10px;">
          <div style="font-weight: 700; font-size: 13px; color: #1d4ed8; text-align: center; margin-bottom: 5px;">Burndown Rodada 3 (312h / 305h)</div>
          <svg viewBox="0 0 400 200" width="100%" height="auto">
            <line x1="50" y1="20" x2="50" y2="160" stroke="#cbd5e1" />
            <line x1="50" y1="160" x2="380" y2="160" stroke="#cbd5e1" />
            <text x="45" y="25" text-anchor="end" font-size="10" fill="#64748b">312h</text>
            <text x="45" y="160" text-anchor="end" font-size="10" fill="#64748b">0h</text>
            <!-- Planejado: [312, 250, 180, 100, 30, 0] -->
            <!-- y: 20, 48, 79, 115, 146, 160 -->
            <polyline points="50,20 116,48 182,79 248,115 314,146 380,160" fill="none" stroke="#94a3b8" stroke-width="2" stroke-dasharray="4,4" />
            <!-- Real: [312, 248, 175, 95, 25, 0] -->
            <!-- y: 20, 49, 81, 117, 149, 160 -->
            <polyline points="50,20 116,49 182,81 248,117 314,149 380,160" fill="none" stroke="#2563eb" stroke-width="2.5" />
            <circle cx="380" cy="160" r="4" fill="#2563eb" />
            <text x="50" y="175" text-anchor="middle" font-size="9" fill="#475569">07/10</text>
            <text x="182" y="175" text-anchor="middle" font-size="9" fill="#475569">17/10</text>
            <text x="380" y="175" text-anchor="middle" font-size="9" fill="#475569">28/10</text>
          </svg>
        </div>

        <!-- Burndown Consolidado Global -->
        <div style="background: #fafafa; border: 1px solid #e2e8f0; border-radius: 6px; padding: 10px;">
          <div style="font-weight: 700; font-size: 13px; color: #4338ca; text-align: center; margin-bottom: 5px;">Burndown Consolidado Acumulado (612h)</div>
          <svg viewBox="0 0 400 200" width="100%" height="auto">
            <line x1="50" y1="20" x2="50" y2="160" stroke="#cbd5e1" />
            <line x1="50" y1="160" x2="380" y2="160" stroke="#cbd5e1" />
            <text x="45" y="25" text-anchor="end" font-size="10" fill="#64748b">612h</text>
            <text x="45" y="160" text-anchor="end" font-size="10" fill="#64748b">0h</text>
            <!-- Planejado: [612, 492, 412, 312, 180, 0] -->
            <!-- y = 160 - (val/612 * 140) -->
            <!-- y: 20, 47, 66, 89, 119, 160 -->
            <polyline points="50,20 116,47 182,66 248,89 314,119 380,160" fill="none" stroke="#94a3b8" stroke-width="2" stroke-dasharray="4,4" />
            <!-- Real: [612, 492, 417, 308, 175, 0] -->
            <!-- y: 20, 47, 65, 90, 120, 160 -->
            <polyline points="50,20 116,47 182,65 248,90 314,120 380,160" fill="none" stroke="#4f46e5" stroke-width="3" />
            <circle cx="380" cy="160" r="5" fill="#4f46e5" />
            <text x="50" y="175" text-anchor="middle" font-size="9" fill="#475569">01/09</text>
            <text x="182" y="175" text-anchor="middle" font-size="9" fill="#475569">25/09</text>
            <text x="380" y="175" text-anchor="middle" font-size="9" fill="#475569">28/10</text>
          </svg>
        </div>

      </div>
    </div>
    """

def build_html():
    print("Carregando e integrando documentos...")
    
    # Textos dos arquivos originais
    doc_visao = read_doc("rodada1/01-documento-visao.md")
    doc_eap_r2 = read_doc("rodada2/02-escopo-eap.md")
    doc_backlog = read_doc("rodada2/01-backlog.md")
    doc_poker = read_doc("rodada1/04-estimativas-planning-poker.md")
    doc_gantt_orc = read_doc("plano-projeto/secao-5-cronograma-orcamento.md")
    doc_monit = read_doc("rodada3/04-monitoramento-consolidado.md")
    doc_riscos = read_doc("rodada2/analise-riscos-atualizada.md")
    doc_versoes = read_doc("plano-projeto/secao-8-controle-versoes.md")
    doc_historico = read_doc("rodada3/02-historico-versoes.md")
    doc_decisoes = read_doc("rodada3/03-dificuldades-decisoes.md")
    doc_demo_v3 = read_doc("rodada3/01-roteiro-demo-v3.md")
    doc_deploy = read_doc("rodada3/deploy-conta-teste.md")
    doc_partic = read_doc("rodada3/05-registro-participacao-consolidado.md")
    doc_retro = read_doc("rodada3/06-retrospectiva-final.md")

    md = markdown.Markdown(extensions=['tables', 'fenced_code', 'toc'])

    def render_md(text):
        # Remove eventuais blocos mermaid de texto puro ja que vamos injetar os vetores SVG
        lines = []
        skip_mermaid = False
        for line in text.splitlines():
            if line.strip().startswith("```mermaid"):
                skip_mermaid = True
                continue
            elif skip_mermaid and line.strip().startswith("```"):
                skip_mermaid = False
                continue
            elif not skip_mermaid:
                lines.append(line)
        return md.convert("\n".join(lines))

    html_content = f"""<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <title>FinApp — Documento Consolidado do Projeto</title>
  <style>
    @page {{
      size: A4;
      margin: 20mm 15mm 20mm 15mm;
      @bottom-right {{
        content: counter(page);
        font-family: 'Segoe UI', sans-serif;
        font-size: 9pt;
        color: #64748b;
      }}
      @bottom-left {{
        content: "FinApp — Projeto de Gerência de Projetos (UFF)";
        font-family: 'Segoe UI', sans-serif;
        font-size: 9pt;
        color: #64748b;
      }}
    }}

    body {{
      font-family: 'Segoe UI', -apple-system, BlinkMacSystemFont, Roboto, Helvetica, Arial, sans-serif;
      color: #1e293b;
      line-height: 1.55;
      font-size: 10.5pt;
      background: #ffffff;
      margin: 0;
      padding: 0;
    }}

    /* Capa Executiva */
    .cover-page {{
      page-break-after: always;
      height: 92vh;
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      border: 3px solid #1e3a8a;
      padding: 40px;
      box-sizing: border-box;
      background: linear-gradient(180deg, #f8fafc 0%, #ffffff 100%);
    }}
    .cover-header {{
      text-align: center;
      border-bottom: 2px solid #cbd5e1;
      padding-bottom: 20px;
    }}
    .cover-header h3 {{
      margin: 0;
      color: #475569;
      font-size: 13pt;
      text-transform: uppercase;
      letter-spacing: 1px;
    }}
    .cover-header h4 {{
      margin: 5px 0 0 0;
      color: #64748b;
      font-size: 11pt;
      font-weight: 500;
    }}
    .cover-title-box {{
      text-align: center;
      margin: auto 0;
    }}
    .cover-title-box h1 {{
      font-size: 28pt;
      color: #1e3a8a;
      margin: 0 0 10px 0;
      font-weight: 800;
      letter-spacing: -0.5px;
    }}
    .cover-title-box h2 {{
      font-size: 15pt;
      color: #0d9488;
      margin: 0 0 20px 0;
      font-weight: 600;
    }}
    .cover-badge {{
      display: inline-block;
      background: #1e3a8a;
      color: #ffffff;
      padding: 6px 18px;
      border-radius: 20px;
      font-size: 10pt;
      font-weight: 600;
      text-transform: uppercase;
      letter-spacing: 0.5px;
    }}
    .cover-footer {{
      border-top: 2px solid #cbd5e1;
      padding-top: 20px;
      display: flex;
      justify-content: space-between;
      font-size: 10pt;
    }}
    .cover-team {{
      line-height: 1.6;
    }}
    .cover-team strong {{
      color: #1e3a8a;
    }}

    /* Estrutura de Capitulos */
    .chapter {{
      page-break-before: always;
      margin-top: 10px;
    }}
    .chapter:first-of-type {{
      page-break-before: avoid;
    }}

    h1 {{
      color: #1e3a8a;
      font-size: 18pt;
      border-bottom: 2px solid #e2e8f0;
      padding-bottom: 8px;
      margin-top: 20px;
      margin-bottom: 15px;
      font-weight: 700;
    }}
    h2 {{
      color: #0f766e;
      font-size: 13.5pt;
      margin-top: 18px;
      margin-bottom: 10px;
      font-weight: 600;
      border-left: 4px solid #0d9488;
      padding-left: 8px;
    }}
    h3 {{
      color: #334155;
      font-size: 11.5pt;
      margin-top: 14px;
      margin-bottom: 6px;
      font-weight: 600;
    }}

    p, ul, ol {{
      margin-top: 0;
      margin-bottom: 10px;
      text-align: justify;
    }}

    /* Tabelas */
    table {{
      width: 100%;
      border-collapse: collapse;
      margin: 14px 0;
      font-size: 9pt;
      page-break-inside: avoid;
    }}
    th {{
      background-color: #1e3a8a;
      color: #ffffff;
      text-align: left;
      padding: 7px 9px;
      font-weight: 600;
      border: 1px solid #1e3a8a;
    }}
    td {{
      padding: 6px 9px;
      border: 1px solid #cbd5e1;
      vertical-align: middle;
    }}
    tr:nth-child(even) {{
      background-color: #f8fafc;
    }}

    /* Caixas de Destaque e Diagramas */
    .diagram-box {{
      border: 1px solid #cbd5e1;
      background: #ffffff;
      border-radius: 8px;
      padding: 15px;
      margin: 18px 0;
      page-break-inside: avoid;
      box-shadow: 0 1px 3px rgba(0,0,0,0.05);
    }}
    .diagram-title {{
      font-size: 11pt;
      font-weight: 700;
      color: #1e3a8a;
      text-align: center;
      margin-bottom: 12px;
      border-bottom: 1px solid #f1f5f9;
      padding-bottom: 6px;
    }}

    blockquote {{
      background: #f0fdf4;
      border-left: 4px solid #10b981;
      margin: 12px 0;
      padding: 8px 14px;
      color: #166534;
      font-size: 9.5pt;
    }}

    code {{
      background-color: #f1f5f9;
      padding: 2px 5px;
      border-radius: 4px;
      font-family: 'Consolas', 'Courier New', monospace;
      font-size: 9pt;
      color: #0f172a;
    }}
    pre {{
      background-color: #0f172a;
      color: #f8fafc;
      padding: 10px;
      border-radius: 6px;
      overflow-x: auto;
      font-size: 8.5pt;
      page-break-inside: avoid;
    }}

    /* Sumario */
    .toc-box {{
      background: #f8fafc;
      border: 1px solid #e2e8f0;
      border-radius: 8px;
      padding: 20px 30px;
      margin: 20px 0;
    }}
    .toc-box ul {{
      list-style-type: none;
      padding-left: 0;
      margin-bottom: 0;
    }}
    .toc-box li {{
      padding: 5px 0;
      border-bottom: 1px dotted #cbd5e1;
      font-size: 10.5pt;
    }}
    .toc-box li a {{
      text-decoration: none;
      color: #1e3a8a;
      font-weight: 600;
    }}
  </style>
</head>
<body>

  <!-- CAPA -->
  <div class="cover-page">
    <div class="cover-header">
      <h3>Universidade Federal Fluminense — UFF</h3>
      <h4>Instituto de Computação · Gerência de Projetos e Manutenção de Software (GPMS)</h4>
      <h4>Professora: Rebeca Campos Motta · Semestre 2026.2</h4>
    </div>

    <div class="cover-title-box">
      <h1>FinApp — Gestão Financeira Pessoal</h1>
      <h2>Plano de Projeto Consolidado e Relatório de Encerramento</h2>
      <div class="cover-badge">Entrega Final · Rodadas 1, 2 e 3 Integradas</div>
    </div>

    <div class="cover-footer">
      <div class="cover-team">
        <strong>Equipe de Desenvolvimento e Gestão:</strong><br>
        • <strong>Filipe Moreira</strong> — Gerente de Projeto (GP) &amp; Dev<br>
        • <strong>Sara Marcomini</strong> — Product Owner (PO)<br>
        • <strong>Emanuel</strong> — Scrum Master<br>
        • <strong>Arben Tafili</strong> — Gerente de Configuração<br>
        • <strong>Enzo</strong> — Desenvolvedor (Infra/DB/Fullstack)<br>
        • <strong>Giovana</strong> — Desenvolvedora (Backend/Frontend)
      </div>
      <div style="text-align: right; line-height: 1.6;">
        <strong>Data de Consolidação:</strong> 28/10/2026<br>
        <strong>Status:</strong> 100% Entregue &amp; Homologado<br>
        <strong>Baseline Global:</strong> 153 SP · 612 h · R$ 30.600<br>
        <strong>Repositório:</strong> GitHub (main branch)
      </div>
    </div>
  </div>

  <!-- SUMARIO -->
  <div class="chapter">
    <h1>Sumário Executivo do Projeto</h1>
    <div class="toc-box">
      <ul>
        <li><strong>Seção 1.</strong> Documento de Visão e Requisitos (RF01 a RF05 e RNF01 a RNF05)</li>
        <li><strong>Seção 2.</strong> Backlog do Produto e User Stories Refinadas</li>
        <li><strong>Seção 3.</strong> Escopo do Projeto e Diagrama Hierárquico da EAP (WBS)</li>
        <li><strong>Seção 4.</strong> Estimativas de Esforço (Planning Poker e Story Points)</li>
        <li><strong>Seção 5.</strong> Cronograma Detalhado (Gantt) com Análise de Prazos e Atrasos</li>
        <li><strong>Seção 6.</strong> Detalhamento Completo do Orçamento (Por EAP e Por Membro)</li>
        <li><strong>Seção 7.</strong> Monitoramento e Controle (EVM Acumulado, Curva S e Burndowns)</li>
        <li><strong>Seção 8.</strong> Gestão de Riscos (Matriz de Riscos e Planos de Mitigação)</li>
        <li><strong>Seção 9.</strong> Decisões Técnicas e Arquitetura (ADR 001, ADR 002 e RDT-01 Docker/Postgres)</li>
        <li><strong>Seção 10.</strong> Gerência de Configuração, Branches e Auditoria do Repositório</li>
        <li><strong>Seção 11.</strong> Histórico de Versões e Integração das Entregas Técnicas</li>
        <li><strong>Seção 12.</strong> Roteiro da Demonstração v3, Publicação em Nuvem e Conta de Teste</li>
        <li><strong>Seção 13.</strong> Registro de Participação Individual e Esforço Consolidado (Horas e Commits)</li>
        <li><strong>Seção 14.</strong> Retrospectiva Final (Roteiro em Vídeo, Lições Aprendidas e Conclusão)</li>
      </ul>
    </div>
  </div>

  <!-- SECAO 1: VISAO -->
  <div class="chapter">
    <h1>1. Documento de Visão e Requisitos</h1>
    {render_md(doc_visao)}
  </div>

  <!-- SECAO 2: BACKLOG -->
  <div class="chapter">
    <h1>2. Backlog do Produto e Priorização</h1>
    {render_md(doc_backlog)}
  </div>

  <!-- SECAO 3: EAP & DIAGRAMA -->
  <div class="chapter">
    <h1>3. Escopo do Projeto e Estrutura Analítica (EAP)</h1>
    {svg_eap_diagram()}
    {render_md(doc_eap_r2)}
  </div>

  <!-- SECAO 4: PLANNING POKER -->
  <div class="chapter">
    <h1>4. Estimativas de Esforço (Planning Poker)</h1>
    {render_md(doc_poker)}
  </div>

  <!-- SECAO 5: CRONOGRAMA & GANTT -->
  <div class="chapter">
    <h1>5. Cronograma Detalhado e Linha do Tempo (Gantt)</h1>
    {svg_gantt_diagram()}
    {render_md(doc_gantt_orc)}
  </div>

  <!-- SECAO 7: MONITORAMENTO, EVM, CURVA S E BURNDOWNS -->
  <div class="chapter">
    <h1>6. Monitoramento e Controle (EVM, Curva S e Burndowns)</h1>
    {svg_curva_s()}
    {svg_burndowns()}
    {render_md(doc_monit)}
  </div>

  <!-- SECAO 8: RISCOS -->
  <div class="chapter">
    <h1>7. Gestão e Análise de Riscos</h1>
    {render_md(doc_riscos)}
  </div>

  <!-- SECAO 9: DECISOES TECNICAS -->
  <div class="chapter">
    <h1>8. Decisões Técnicas e Arquitetura (ADRs &amp; RDT-01)</h1>
    {render_md(doc_decisoes)}
  </div>

  <!-- SECAO 10: CONFIGURACAO E BRANCHES -->
  <div class="chapter">
    <h1>9. Gerência de Configuração e Controle de Versões</h1>
    {render_md(doc_versoes)}
  </div>

  <!-- SECAO 11: HISTORICO DE VERSOES -->
  <div class="chapter">
    <h1>10. Histórico de Versões e Entregas</h1>
    {render_md(doc_historico)}
  </div>

  <!-- SECAO 12: DEMO V3 E DEPLOY -->
  <div class="chapter">
    <h1>11. Demonstração do Produto (Demo v3) e Deploy Cloud</h1>
    {render_md(doc_demo_v3)}
    {render_md(doc_deploy)}
  </div>

  <!-- SECAO 13: REGISTRO DE PARTICIPACAO -->
  <div class="chapter">
    <h1>12. Registro de Participação Individual e Esforço</h1>
    {render_md(doc_partic)}
  </div>

  <!-- SECAO 14: RETROSPECTIVA FINAL -->
  <div class="chapter">
    <h1>13. Retrospectiva Final e Encerramento</h1>
    {render_md(doc_retro)}
  </div>

</body>
</html>
"""

    with open(OUTPUT_HTML, "w", encoding="utf-8") as f:
        f.write(html_content)
    print(f"HTML Consolidado gerado em: {OUTPUT_HTML}")

    # Geracao do PDF via Microsoft Edge Headless
    edge_path = r"C:\Program Files (x86)\Microsoft\Edge\Application\msedge.exe"
    if not os.path.exists(edge_path):
        edge_path = r"C:\Program Files\Microsoft\Edge\Application\msedge.exe"

    print("Gerando PDF via Microsoft Edge Headless...")
    cmd = [
        edge_path,
        "--headless",
        "--disable-gpu",
        "--run-all-compositor-stages-before-draw",
        f"--print-to-pdf={OUTPUT_PDF}",
        f"file:///{OUTPUT_HTML.replace(os.sep, '/')}"
    ]

    res = subprocess.run(cmd, capture_output=True, text=True)
    if os.path.exists(OUTPUT_PDF) and os.path.getsize(OUTPUT_PDF) > 1000:
        # Copiar tambem para docs/
        import shutil
        shutil.copyfile(OUTPUT_PDF, OUTPUT_PDF_DOCS)
        print(f"PDF consolidado gerado com sucesso!")
        print(f"1. Raiz: {OUTPUT_PDF} ({os.path.getsize(OUTPUT_PDF)} bytes)")
        print(f"2. Docs: {OUTPUT_PDF_DOCS}")
        return True
    else:
        print("Erro ao gerar PDF:", res.stderr)
        return False

if __name__ == "__main__":
    build_html()
